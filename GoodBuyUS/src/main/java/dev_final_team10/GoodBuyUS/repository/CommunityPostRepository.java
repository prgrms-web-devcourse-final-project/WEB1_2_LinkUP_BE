package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;

import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findAllByUserId(Long id);

    List<CommunityPost> findByNeighborhood(Neighborhood neighborhood);
}
