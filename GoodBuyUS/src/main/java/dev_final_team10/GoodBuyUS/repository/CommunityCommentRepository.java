package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    }
