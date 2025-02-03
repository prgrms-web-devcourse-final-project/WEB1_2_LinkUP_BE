package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.community.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    List<UserReview> findByUser_Id(Long id);

    @Query("SELECT AVG((r.question1Score + r.question2Score + r.question3Score) / 3.0) FROM UserReview r WHERE r.userId = :userId")
    Double findOverallAverageScore(@Param("userId") Long userId);

}
