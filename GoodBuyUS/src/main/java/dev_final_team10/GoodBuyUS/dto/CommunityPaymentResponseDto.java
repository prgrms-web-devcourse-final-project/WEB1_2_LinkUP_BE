package dev_final_team10.GoodBuyUS.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev_final_team10.GoodBuyUS.domain.PayType;
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
    private PayType payType;

    @JsonProperty("virtualAccount")
    private VirtualAccount virtualAccount;

    private Integer totalAmount;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
      //  private String dueDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Receipt {
        private String url;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Checkout {
        private String url;
    }
}

