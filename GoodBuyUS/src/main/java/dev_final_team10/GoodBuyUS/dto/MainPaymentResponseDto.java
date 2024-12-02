package dev_final_team10.GoodBuyUS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPaymentResponseDto {
    private String orderId; // 고유 주문 번호
    private String productName; // 상품 이름
    private int quantity; // 주문 수량
    private int price; // 상품 개당 가격
    private int totalPrice; // 총 결제 금액
    private String paymentStatus; // 결제 상태
    private String paymentKey; // Toss Payments 결제 키
    private LocalDateTime createdAt; // 주문 생성 시간
    private LocalDateTime updatedAt; // 주문 업데이트 시간
    private String paymentPageUrl; // 결제 페이지 URL
}
