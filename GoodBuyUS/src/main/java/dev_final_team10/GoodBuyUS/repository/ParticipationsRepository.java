package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationsRepository extends JpaRepository<Participations, Long> {
    List<Participations> findAllByCommunityPost_CommunityPostIdAndStatus(Long communityPostId, participationStatus status);
    List<Participations> findAllByCommunityPost_CommunityPostId(Long communityPostId);
}
