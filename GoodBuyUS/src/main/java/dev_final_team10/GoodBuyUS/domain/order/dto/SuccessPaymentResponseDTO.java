package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data @AllArgsConstructor @NoArgsConstructor
public class SuccessPaymentResponseDTO {
    private PaymentStatus paymentStatus = PaymentStatus.SUCCESS;
    private Long postId;
    private UUID orderId;
    private int amount;

    public SuccessPaymentResponseDTO(Long postId, UUID orderId, int amount) {
        this.postId = postId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
