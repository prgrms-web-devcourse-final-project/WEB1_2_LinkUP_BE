package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @AllArgsConstructor @NoArgsConstructor
public class OrdersDTO {
    private String productName;
    private int pirce;
    private LocalDateTime orderDate;
    private PaymentStatus paymentStatus;
    private Long paymentId;
}
