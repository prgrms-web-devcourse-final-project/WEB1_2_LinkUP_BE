package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.dto.UserReviewDto;
import dev_final_team10.GoodBuyUS.service.UserReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/community/review")
public class UserReviewController {

    private final UserReviewService userReviewService;
    //사용자 리뷰 등록
    @PostMapping
    public UserReviewDto writeUserReview(@RequestBody UserReviewDto userReviewDto){
        return userReviewService.writeUserReview(userReviewDto);
    }
}
