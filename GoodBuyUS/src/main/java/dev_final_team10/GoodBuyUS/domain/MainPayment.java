package dev_final_team10.GoodBuyUS.domain;

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

    private String orderId; // 고유 주문 번호

    private String productName; // 상품 이름

    private int quantity; // 주문 수량

    private int price; // 상품 개당 가격

    private int totalPrice; // 총 결제 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태

    private String paymentKey; // Toss Payments에서 반환된 결제 키

    private LocalDateTime createdAt; // 주문 생성 시간

    private LocalDateTime updatedAt; // 주문 업데이트 시간

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id")
    private MainDelivery mainDelivery; // 배송 정보
}
