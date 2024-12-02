package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.MainPayment;
import dev_final_team10.GoodBuyUS.domain.PaymentStatus;
import dev_final_team10.GoodBuyUS.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.dto.MainPaymentResponseDto;
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

    @Transactional
    public MainPaymentResponseDto createAndRequestPayment(MainPaymentRequestDto requestDto) {
        WebClient webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .build();

        // 요청으로 받은 orderId 사용
        String orderId = requestDto.getOrderId();

        // 결제 데이터 생성 및 저장
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
            // Toss API에 결제 요청
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

            // Toss API 응답 로그 출력
            log.info("Toss API 응답: {}", rawResponse);

            // 응답 데이터 처리
            Map<String, Object> responseMap = objectMapper.readValue(rawResponse, Map.class);
            String paymentPageUrl = (String) responseMap.get("checkoutPageUrl");

            // 응답 DTO 생성 및 반환
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

    @Transactional
    public void handlePaymentSuccess(String paymentKey, String orderId, int amount) {
        log.info("결제 성공 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        MainPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (payment.getTotalPrice() != amount) {
            throw new RuntimeException("결제 금액이 일치하지 않습니다.");
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentKey(paymentKey);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("결제 성공 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);
    }

}
