package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Payment findByOrder_OrderId(UUID orderId);
    public Payment findByOrder(Order order);
}
