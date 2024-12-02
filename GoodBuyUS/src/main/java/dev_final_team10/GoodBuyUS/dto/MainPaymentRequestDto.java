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
    private String productName; // 상품 이름
    private int quantity; // 주문 수량
    private int price; // 상품 개당 가격
    private int totalPrice; // 총 결제 금액
    private String successUrl; // 성공 URL
    private String failUrl; // 실패 URL
}
