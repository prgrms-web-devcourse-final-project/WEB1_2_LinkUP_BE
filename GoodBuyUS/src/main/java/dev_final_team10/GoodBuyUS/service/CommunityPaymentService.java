package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.CommunityPayment;
import dev_final_team10.GoodBuyUS.domain.PayType;
import dev_final_team10.GoodBuyUS.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.repository.CommunityPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CommunityPaymentService {

    private final WebClient webClient;
    private final CommunityPaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    public CommunityPaymentService(WebClient.Builder webClientBuilder,
                                   CommunityPaymentRepository paymentRepository,
                                   ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("test_sk_LlDJaYngrooZolGP9mERrezGdRpX", "");
                    headers.set("Content-Type", "application/json");
                })
                .build();
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
    }

    public CommunityPaymentResponseDto createAndRequestPayment(CommunityPaymentRequestDto requestDto) {
        try {
            String rawResponse = webClient.post()
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Toss API 응답 데이터: " + rawResponse);

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);
            System.out.println("Parsed paymentKey: " + responseDto.getPaymentKey());

            CommunityPayment payment = CommunityPayment.builder()
                    .participationsOrderId(requestDto.getOrderId())
                    .amount(requestDto.getAmount())
                    .paymentStatus("WAITING_FOR_APPROVAL")
                    .communityPaymentKey(responseDto.getPaymentKey())
                    .payType(requestDto.getPayType())
                    .communityCreatedAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            return responseDto;

        } catch (Exception e) {
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }


    public CommunityPaymentResponseDto confirmAndSavePayment(String paymentKey, String orderId, int amount) {
        try {
            String rawResponse = webClient.post()
                    .uri("/" + paymentKey)
                    .bodyValue(Map.of(
                            "amount", amount,
                            "orderId", orderId
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);

            CommunityPayment payment = paymentRepository.findByParticipationsOrderId(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다: " + orderId));

            payment = payment.toBuilder()
                    .paymentStatus(responseDto.getStatus())
                    .communityPaymentKey(responseDto.getPaymentKey())
                    .accountNumber(responseDto.getVirtualAccount().getAccountNumber())
                    .bankId(responseDto.getVirtualAccount().getBankCode())
                    .customerName(responseDto.getVirtualAccount().getCustomerName())
                    .communityApprovedAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("결제 승인 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
    public void updatePaymentStatus(String paymentKey) {
        try {
            String rawResponse = webClient.get()
                    .uri("/" + paymentKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);

            CommunityPayment payment = paymentRepository.findByCommunityPaymentKey(paymentKey)
                    .orElseThrow(() -> new IllegalArgumentException("해당 결제를 찾을 수 없습니다: " + paymentKey));

            payment = payment.toBuilder()
                    .paymentStatus(responseDto.getStatus())
                    .communityApprovedAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("결제 상태 업데이트 중 오류 발생: " + e.getMessage(), e);
        }
    }


}