package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FailedPaymentResponseDTO {
    private PaymentStatus paymentStatus = PaymentStatus.FAIL;
    private String message;

    public FailedPaymentResponseDTO(String message) {
        this.message = message;
    }
}
