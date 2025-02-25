package dev_final_team10.GoodBuyUS.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserReviewResponse {
    private Long userId;    //사용자 아이디
    private double rating;  //평점 평균
    private List<UserReviewDTO> reviews;    //상세 리뷰 목록

    @Getter
    @AllArgsConstructor
    public static class UserReviewDTO {
        private Long reviewerId;    //리뷰 작성자 아이디
        private String nickname;    //리뷰 작성자 닉네임
        private String reviewContent;   //상세 리뷰
    }
}
