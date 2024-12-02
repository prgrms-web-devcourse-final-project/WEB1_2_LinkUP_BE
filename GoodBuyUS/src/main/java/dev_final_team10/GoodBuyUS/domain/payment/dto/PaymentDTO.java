package dev_final_team10.GoodBuyUS.domain.payment.dto;

import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {
    private PaymentStatus status;
    private String paymentKey;
    private UUID orderId;
    private int amount;
    private String message;
    private String redirectURL;

    public PaymentDTO(PaymentStatus status, String paymentKey, UUID orderId, int amount) {
        this.status = status;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public PaymentDTO(PaymentStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
