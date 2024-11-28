package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Address;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String url;
    private String productName;
    private int price;
    private int quantity;
    private DeliveryRequestDTO deliveryRequestDTO;

    @Data
    public static class DeliveryRequestDTO {
        private Address address;
        private String needed;
    }
}

