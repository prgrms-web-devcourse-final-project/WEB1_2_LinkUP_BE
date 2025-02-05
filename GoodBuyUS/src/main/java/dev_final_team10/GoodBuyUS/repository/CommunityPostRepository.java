package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;

import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findAllByUserIdAndStatusNot(Long id, postStatus status);

    List<CommunityPost> findByNeighborhood(Neighborhood neighborhood);

    CommunityPost findByCommunityPostId(Long id);

    @Query("""
    SELECT c.availableNumber - COALESCE(SUM(p.quantity), 0)
    FROM CommunityPost c
    LEFT JOIN Participations p 
        ON c.communityPostId = p.communityPost.communityPostId 
        AND p.status IN  (:statuses)
    WHERE c.communityPostId = :communityId
    GROUP BY c.communityPostId, c.availableNumber
""")
    Long findRemainingQuantity(@Param("communityId") Long communityId,
                               @Param("statuses") List<participationStatus> statuses);

}
