package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.payment.dto.TossWebhookDto;
import dev_final_team10.GoodBuyUS.domain.payment.entity.CommunityPayment;
import dev_final_team10.GoodBuyUS.repository.CommunityPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// 아래는 웹훅관련 기능입니다. 켜두면 계속 요청을 보내서 필요하신 분만 쓰라고 주석처리 했습니다.

@Service
public class CommunityWebhookService {

    private final CommunityPaymentRepository paymentRepository;
    private final SSEService sseService;

    public CommunityWebhookService(CommunityPaymentRepository paymentRepository, SSEService sseService) {
        this.paymentRepository = paymentRepository;
        this.sseService = sseService;
    }

    @Transactional
    public void processWebhook(TossWebhookDto webhookDto) {
        String status;
        String paymentKey;
        String orderId;
        String secret;

        // 긴 형식 (data 객체가 있는 경우)
        if (webhookDto.getData() != null) {
            TossWebhookDto.Data data = webhookDto.getData();
            status = data.getStatus();
            paymentKey = data.getPaymentKey();
            orderId = data.getOrderId();
            secret = data.getSecret();
        }
        // 짧은 형식 (data 객체가 없는 경우)
        else {
            status = webhookDto.getStatus();
            paymentKey = webhookDto.getPaymentKey();
            orderId = webhookDto.getOrderId();
            secret = webhookDto.getSecret();
        }
// 결제 정보 조회: paymentKey 조회 없으면 orderId로 조회 총 2개 형태로 요청이 가는데 주문 id가 없는 경우가 있습니다.
        CommunityPayment payment;

        if (paymentKey != null) {
            payment = paymentRepository.findByCommunityPaymentKey(paymentKey)
                    .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + paymentKey));
        } else {
            payment = paymentRepository.findByParticipationsOrderId(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + orderId));
        }

        if (!secret.equals(payment.getSecret())) {
            throw new SecurityException("Invalid webhook secret");
        }

        switch (status) {
            case "DONE":
                payment = payment.toBuilder()
                        .paymentStatus("COMPLETED")
                        .communityApprovedAt(LocalDateTime.now())
                        .build();
                sseService.sendEvent(payment.getCommunityPaymentKey(), "결제가 완료되었습니다.");
                break;

            case "CANCELED":
                payment = payment.toBuilder()
                        .paymentStatus("CANCELED")
                        .build();
                sseService.sendEvent(payment.getCommunityPaymentKey(), "결제가 취소되었습니다.");
                break;

            case "WAITING_FOR_DEPOSIT":
                payment = payment.toBuilder()
                        .paymentStatus("WAITING_FOR_DEPOSIT")
                        .build();
                sseService.sendEvent(payment.getCommunityPaymentKey(), "입금 대기 상태입니다.");
                break;

            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }

        paymentRepository.save(payment);
    }
}
