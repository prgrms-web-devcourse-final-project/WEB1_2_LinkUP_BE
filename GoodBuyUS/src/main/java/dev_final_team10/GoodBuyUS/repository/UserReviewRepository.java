package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
}
