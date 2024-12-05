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
public class TossWebhookDto {

    private String createdAt;
    private String eventType;
    private String status; // 결제 상태 (DONE, WAITING_FOR_DEPOSIT 토스에서 주는 형태)
    private String paymentKey;
    private String orderId;
    private String secret; // 요청 검증용 Secret
    private String transactionKey; // 짧은 형식에서 사용

    @JsonProperty("data")
    private Data data; // 긴형태에서 사용

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String paymentKey;
        private String orderId;
        private String status;
        private String secret;

        @JsonProperty("virtualAccount")
        private VirtualAccount virtualAccount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
    }
}

