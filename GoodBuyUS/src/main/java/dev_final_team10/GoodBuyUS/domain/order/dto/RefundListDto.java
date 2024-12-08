package dev_final_team10.GoodBuyUS.domain.order.dto;

import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class RefundListDto {
    private String productName;
    private int quantity;
    private int price;
    private String url;
    private PaymentStatus paymentStatus;
    private Long postId;
}
