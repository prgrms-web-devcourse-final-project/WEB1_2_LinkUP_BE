package dev_final_team10.GoodBuyUS.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPaymentResponseDto {
    private String orderId;
    private String productName;
    private int quantity;
    private int price;
    private int totalPrice;
    private String paymentStatus;
    private String paymentKey;
    private String cancelReason;
    private int refundedAmount;
    private int balanceAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentPageUrl;
}
