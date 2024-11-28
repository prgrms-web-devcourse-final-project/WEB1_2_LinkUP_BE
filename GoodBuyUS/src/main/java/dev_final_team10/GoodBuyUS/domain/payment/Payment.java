package dev_final_team10.GoodBuyUS.domain.payment;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")  // 외래 키 관리
    private Order order;

    private int price;
    public Payment(PaymentStatus paymentStatus, Order order, int price) {
        this.paymentStatus = paymentStatus;
        this.order = order;
        this.price = price;
    }

    public Payment(PaymentStatus paymentStatus, Order order) {
        this.paymentStatus = paymentStatus;
        this.order = order;
    }

    public void changeStatus(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }
}
