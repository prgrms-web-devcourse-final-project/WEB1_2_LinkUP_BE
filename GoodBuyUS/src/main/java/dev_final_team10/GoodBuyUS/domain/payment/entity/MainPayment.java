package dev_final_team10.GoodBuyUS.domain.payment.entity;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "main_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")  // 외래 키 관리
    private Order order;

    private String productName; // 상품 이름

    private int quantity; // 주문 수량

    private int price; // 상품 개당 가격

    private int totalPrice; // 총 결제 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentKey;

    private String cancelReason;

    private int refundedAmount;

    private int balanceAmount; // 잔액 (환불 가능 금액)

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
