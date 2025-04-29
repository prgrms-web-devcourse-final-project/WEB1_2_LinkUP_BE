package dev_final_team10.GoodBuyUS.domain.community.dto;

import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.UserReview;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReviewDto {
    private Long reviewerId;
    private Long hostId;
    private int question1Score;
    private int question2Score;
    private int question3Score;
    private String content;

    //DTO -> Entity
    public UserReview toEntityForCreate(UserReviewDto dto, User host,  Participations reviewer) {
        return UserReview.builder()
                .reviewer(reviewer)
                .host(host)
                .question1Score(dto.question1Score)
                .question2Score(dto.question2Score)
                .question3Score(dto.question3Score)
                .content(dto.content)
                .reviewDate(LocalDateTime.now())
                .build();
    }
}
