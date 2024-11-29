package dev_final_team10.GoodBuyUS.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
public class RefundDTO {
    private UUID orderId; // 주문 번호
    private Integer amount; // 환불할 금액
    private LocalDateTime refundRequestDate; // 환불 요청 날짜
}
