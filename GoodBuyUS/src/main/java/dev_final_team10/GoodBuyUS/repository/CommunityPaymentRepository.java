package dev_final_team10.GoodBuyUS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev_final_team10.GoodBuyUS.domain.CommunityPayment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityPaymentRepository extends JpaRepository<CommunityPayment, Long> {
    Optional<CommunityPayment> findByParticipationsOrderId(String participationsOrderId);
    Optional<CommunityPayment> findByCommunityPaymentKey(String communityPaymentKey);
}
