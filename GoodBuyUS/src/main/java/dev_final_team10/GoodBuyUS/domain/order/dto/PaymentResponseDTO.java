package dev_final_team10.GoodBuyUS.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentResponseDTO<T> {
    private T data;
}
