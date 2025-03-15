package dev_final_team10.GoodBuyUS.repository;

import com.sun.tools.javac.Main;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.payment.entity.MainPayment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MainPaymentRepository extends JpaRepository<MainPayment, Long> {

    Optional<MainPayment> findByOrder(Order order);

    @EntityGraph(attributePaths = {"order"})
    MainPayment findWithByOrder(Order order);

    Optional<MainPayment> findByPaymentKey(String paymentKey);
}
