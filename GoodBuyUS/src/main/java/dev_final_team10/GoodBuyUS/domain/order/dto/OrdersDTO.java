package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class OrdersDTO {
    private String productName;
    private int pirce;
    private LocalDateTime orderDate;
    private PaymentStatus paymentStatus;
    private Long paymentId;
    private int quantity;
}
