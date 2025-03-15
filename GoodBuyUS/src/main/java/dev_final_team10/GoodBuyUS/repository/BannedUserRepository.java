package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.user.entity.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
    List<BannedUser> findByBanEndBefore(LocalDateTime now);
}
