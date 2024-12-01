package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
}
