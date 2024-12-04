package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationsRepository extends JpaRepository<Participations, Integer> {
    List<Participations> findByCommunityPost(CommunityPost post);
}
