package dev_final_team10.GoodBuyUS.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityPaymentResponseDto {
    private String paymentKey;
    private String orderId;
    private String status;
    private Integer totalAmount;

    @JsonProperty("secret")
    private String secret; // 요청 검증용 Secret 웹훅에서 필요해요

    @JsonProperty("virtualAccount")
    private VirtualAccount virtualAccount;



    @JsonProperty("cancelAmount")
    private Integer cancelAmount;

    @JsonProperty("cancelReason")
    private String cancelReason;

    @JsonProperty("refundStatus")
    private String refundStatus;

    @JsonProperty("refundAvailableAmount")
    private Integer refundAvailableAmount;

    @JsonProperty("canceledAt")
    private String canceledAt;



    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
    }

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

