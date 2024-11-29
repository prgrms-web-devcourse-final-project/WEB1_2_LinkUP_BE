package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Address;
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
    private String payMethod;
    @Data
    public static class DeliveryRequestDTO {
        private String name;
        private Address address;
        private String needed;
    }
}

