package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentRequestDTO {
    private String url;
    private String productName;
    // 개별 가격(생성시점에서 검사)
    private int price;
    private int amount;
    // 개별 * 수량 가격
    private int finalPrice;
    private Address address;
    private String needed;
    private String payMethod;
    private Long postId;
}
