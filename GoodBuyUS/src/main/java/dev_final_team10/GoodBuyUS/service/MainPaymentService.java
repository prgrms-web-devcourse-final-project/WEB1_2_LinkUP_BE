package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.payment.entity.MainPayment;
import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.repository.MainPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPaymentService {

    private final WebClient.Builder webClientBuilder;
    private final MainPaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    //결제 요청
    @Transactional
    public MainPaymentResponseDto createAndRequestPayment(MainPaymentRequestDto requestDto) {
        WebClient webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .build();

        String orderId = requestDto.getOrderId();

        MainPayment payment = MainPayment.builder()
                .orderId(orderId)
                .productName(requestDto.getProductName())
                .quantity(requestDto.getQuantity())
                .price(requestDto.getPrice())
                .totalPrice(requestDto.getTotalPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        try {
            String rawResponse = webClient.post()
                    .bodyValue(Map.of(
                            "orderId", orderId,
                            "amount", payment.getTotalPrice(),
                            "orderName", payment.getProductName(),
                            "successUrl", requestDto.getSuccessUrl(),
                            "failUrl", requestDto.getFailUrl(),
                            "method", "CARD" // 카드 결제 방식 추가
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Toss API 응답: {}", rawResponse);

            Map<String, Object> responseMap = objectMapper.readValue(rawResponse, Map.class);
            String paymentPageUrl = (String) responseMap.get("checkoutPageUrl");

            return MainPaymentResponseDto.builder()
                    .orderId(orderId)
                    .productName(payment.getProductName())
                    .quantity(payment.getQuantity())
                    .price(payment.getPrice())
                    .totalPrice(payment.getTotalPrice())
                    .paymentStatus(payment.getPaymentStatus().name())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .paymentPageUrl(paymentPageUrl)
                    .build();

        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
    //결제 요청 성공
    @Transactional
    public void handlePaymentSuccess(String paymentKey, String orderId, int amount) {
        log.info("결제 성공 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        MainPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (payment.getTotalPrice() != amount) {
            throw new RuntimeException("결제 금액이 일치하지 않습니다.");
        }

        payment.setPaymentStatus(PaymentStatus.AUTH_COMPLETED);
        payment.setPaymentKey(paymentKey);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("결제 성공 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);
    }
    //결제 승인
    @Transactional
    public void approvePayment(String paymentKey, String orderId, int amount) {
        log.info("결제 승인 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        MainPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (!payment.getPaymentStatus().equals(PaymentStatus.AUTH_COMPLETED)) {
            throw new RuntimeException("이미 승인된 결제이거나 승인할 수 없는 상태입니다.");
        }

        try {
            WebClient webClient = webClientBuilder.build();
            String response = webClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .bodyValue(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", orderId,
                            "amount", amount
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("결제 승인 응답: {}", response);

            payment.setPaymentStatus(PaymentStatus.DONE);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            log.info("결제 승인 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);

        } catch (Exception e) {
            log.error("결제 승인 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 승인 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
    //결제 취소 및 환불
    @Transactional
    public void cancelPayment(String paymentKey, String cancelReason, Integer cancelAmount) {
        log.info("결제 취소 요청: paymentKey={}, cancelReason={}, cancelAmount={}", paymentKey, cancelReason, cancelAmount);

        MainPayment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (!payment.getPaymentStatus().equals(PaymentStatus.DONE) &&
                !payment.getPaymentStatus().equals(PaymentStatus.PARTIAL_CANCELED)) {
            throw new RuntimeException("취소할 수 없는 상태입니다.");
        }

        int effectiveCancelAmount = cancelAmount != null ? cancelAmount : payment.getTotalPrice();
        if (payment.getRefundedAmount() + effectiveCancelAmount > payment.getTotalPrice()) {
            throw new RuntimeException("취소 금액이 총 결제 금액을 초과할 수 없습니다.");
        }

        try {
            WebClient webClient = webClientBuilder.build();
            Map<String, Object> requestBody = Map.of(
                    "cancelReason", cancelReason,
                    "cancelAmount", effectiveCancelAmount
            );

            String response = webClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/{paymentKey}/cancel", paymentKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("결제 취소 응답: {}", response);

            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            String apiResponseStatus = (String) responseMap.get("status");
            if ("CANCELED".equals(apiResponseStatus)) {
                payment.setPaymentStatus(PaymentStatus.CANCELED);
            } else if ("PARTIAL_CANCELED".equals(apiResponseStatus)) {
                payment.setPaymentStatus(PaymentStatus.PARTIAL_CANCELED);
            }

            Integer updatedBalanceAmount = (Integer) responseMap.get("balanceAmount");
            if (updatedBalanceAmount != null) {
                payment.setBalanceAmount(updatedBalanceAmount);
            }

            payment.setRefundedAmount(payment.getRefundedAmount() + effectiveCancelAmount);
            payment.setCancelReason(cancelReason);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            log.info("결제 취소 처리 완료: PaymentKey = {}", paymentKey);

        } catch (Exception e) {
            log.error("결제 취소 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 취소 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
