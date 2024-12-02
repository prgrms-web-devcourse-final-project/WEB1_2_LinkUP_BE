package dev_final_team10.GoodBuyUS.dto;

import dev_final_team10.GoodBuyUS.domain.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPaymentRequestDto {
    private String orderId;
    private int amount;
    private String orderName;
    private String customerName;
    private String customerEmail;
    private String successUrl;
    private String failUrl;
    private String method;
    private String bank;
    private PayType payType;
}
