package dev_final_team10.GoodBuyUS.domain.community.entity;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userReviewId;  //기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id")
    private Participations reviewer;    //리뷰 작성자(공구 참여자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  //공구 주최자(리뷰를 받는 사람)

    private int question1Score;
    private int question2Score;
    private int question3Score; //3가지 점수 선택 문항 (1점~5점)

    private String content; //상세 리뷰

    private LocalDateTime reviewDate;   //리뷰작성일
}
