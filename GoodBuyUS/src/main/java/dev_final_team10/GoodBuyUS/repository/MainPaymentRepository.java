package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.MainPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainPaymentRepository extends JpaRepository<MainPayment, Long> {

    Optional<MainPayment> findByOrderId(String orderId);
}
