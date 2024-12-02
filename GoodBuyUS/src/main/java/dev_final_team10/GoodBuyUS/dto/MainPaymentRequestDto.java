package dev_final_team10.GoodBuyUS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPaymentRequestDto {
    private String orderId;
    private String productName;
    private int quantity;
    private int price;
    private int totalPrice;
    private String successUrl;
    private String failUrl;
}
