package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDTO {
    private String url;
    private String productName;
    // 할인가
    private int price;
    private int discountPrice;
    // 구매 수량
    private int amount;
}
