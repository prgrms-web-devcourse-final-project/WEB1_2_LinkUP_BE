package dev_final_team10.GoodBuyUS.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPaymentResponseDto {
    private UUID orderId;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Checkout checkout;

    public String getCheckoutPageUrl() {
        return this.checkout != null ? this.checkout.getUrl() : null;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Checkout {
        private String url;
    }
}