package dev_final_team10.GoodBuyUS.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @AllArgsConstructor @NoArgsConstructor
public class TossRequestDTO {
    private UUID orderId;
    private Long userId;
    private Long postId;
    private int amount;
    private int quantity;
}
