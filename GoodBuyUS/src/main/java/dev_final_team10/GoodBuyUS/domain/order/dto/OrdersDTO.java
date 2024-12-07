package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Delivery;
import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class OrdersDTO {
    private String productName;
    private int price;
    private LocalDateTime orderDate;
    private PaymentStatus paymentStatus;
    private String payment_key;
    private int quantity;
    private Delivery delivery;
    private Long postId;
    private String url;

    public static OrdersDTO of(String productName, int price, LocalDateTime orderDate, PaymentStatus paymentStatus, String payment_key, int quantity, Delivery delivery, Long postId, String url){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.productName = productName;
        ordersDTO.price = price;
        ordersDTO.orderDate = orderDate;
        ordersDTO.paymentStatus = paymentStatus;
        ordersDTO.payment_key = payment_key;
        ordersDTO.quantity = quantity;
        ordersDTO.delivery =delivery;
        ordersDTO.postId = postId;
        ordersDTO.url = url;
        return ordersDTO;
    }
}
