package dev_final_team10.GoodBuyUS.repository;

<<<<<<< HEAD
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationsRepository extends JpaRepository<Participations, Long> {
    List<Participations> findAllByCommunityPost_CommunityPostIdAndStatus(Long communityPostId, participationStatus status);
    List<Participations> findAllByCommunityPost_CommunityPostId(Long communityPostId);
=======
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationsRepository extends JpaRepository<Participations, Long> {
    List<Participations> findByCommunityPost(CommunityPost post);
>>>>>>> 35cc3a3fb06474bc92a2ba3bf806ed62c2182977
}
