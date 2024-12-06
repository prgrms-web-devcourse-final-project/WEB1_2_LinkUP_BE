package dev_final_team10.GoodBuyUS.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityPaymentResponseDto {
    private String paymentKey;
    private String orderId;
    private String status;
    private Integer totalAmount;
//배송정보
    private String recipientName;
    private String recipientAddress;
    private String deliveryRequest;

    @JsonProperty("secret")
    private String secret; // 요청 검증용 Secret 웹훅에서 필요해요

    @JsonProperty("virtualAccount")
    private VirtualAccount virtualAccount;


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


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelInfo {
        private String transactionKey;
        private String cancelReason;
        private String canceledAt;
        private String cancelStatus;
        private int cancelAmount;
        private int refundableAmount;
    }

}



