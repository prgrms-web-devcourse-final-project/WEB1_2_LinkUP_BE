package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.MainDelivery;
import dev_final_team10.GoodBuyUS.domain.MainPayment;
import dev_final_team10.GoodBuyUS.domain.PaymentStatus;
import dev_final_team10.GoodBuyUS.dto.MainDeliveryResponseDto;
import dev_final_team10.GoodBuyUS.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.repository.MainDeliveryRepository;
import dev_final_team10.GoodBuyUS.repository.MainPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPaymentService {

    private final WebClient.Builder webClientBuilder;
    private final MainPaymentRepository paymentRepository;
    private final MainDeliveryRepository deliveryRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public MainPaymentResponseDto createAndRequestPayment(MainPaymentRequestDto requestDto) {
        // WebClient 인스턴스 생성
        WebClient webClient = webClientBuilder
                .baseUrl("https://sandbox-api.tosspayments.com/v1/payments")
                .defaultHeaders(headers -> headers.setBasicAuth("test_sk_LlDJaYngrooZolGP9mERrezGdRpX", ""))
                .build();

        String orderId = UUID.randomUUID().toString();

        MainDelivery delivery = MainDelivery.builder()
                .recipientName(requestDto.getDeliveryRequest().getRecipientName())
                .street(requestDto.getDeliveryRequest().getStreet())
                .detail(requestDto.getDeliveryRequest().getDetail())
                .specialRequest(requestDto.getDeliveryRequest().getSpecialRequest())
                .build();
        deliveryRepository.save(delivery);

        MainPayment payment = MainPayment.builder()
                .orderId(orderId)
                .productName(requestDto.getProductName())
                .quantity(requestDto.getQuantity())
                .price(requestDto.getPrice())
                .totalPrice(requestDto.getTotalPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .mainDelivery(delivery)
                .build();
        paymentRepository.save(payment);

        try {
            String rawResponse = webClient.post()
                    .bodyValue(Map.of(
                            "orderId", payment.getOrderId(),
                            "amount", payment.getTotalPrice(),
                            "orderName", payment.getProductName(),
                            "successUrl", "http://localhost:8080/api/v1/main-payments/success",
                            "failUrl", "http://localhost:8080/api/v1/main-payments/fail"
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

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
                    .deliveryInfo(MainDeliveryResponseDto.builder()
                            .recipientName(delivery.getRecipientName())
                            .street(delivery.getStreet())
                            .detail(delivery.getDetail())
                            .specialRequest(delivery.getSpecialRequest())
                            .build())
                    .paymentPageUrl(paymentPageUrl)
                    .build();

        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생", e);
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handlePaymentSuccess(String paymentKey, String orderId, int amount) {
        MainPayment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (payment.getTotalPrice() != amount) {
            throw new RuntimeException("결제 금액 불일치");
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentKey(paymentKey);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("결제 성공 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);
    }
}
