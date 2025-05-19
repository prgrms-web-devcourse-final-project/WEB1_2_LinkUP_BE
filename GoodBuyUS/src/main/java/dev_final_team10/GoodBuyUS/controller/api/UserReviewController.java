package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.dto.UserReviewDto;
import dev_final_team10.GoodBuyUS.service.UserReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/community/review")
public class UserReviewController {

    private final UserReviewService userReviewService;
    //사용자 리뷰 등록
    @PostMapping
    public ResponseEntity<?> writeUserReview(@RequestBody UserReviewDto userReviewDto){
        try {
            UserReviewDto savedReview = userReviewService.writeUserReview(userReviewDto);
            return ResponseEntity.ok(savedReview);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage()); // "이미 이 사용자에 대한 리뷰를 작성하였습니다."
            return ResponseEntity.badRequest().body(error);
        }    }
}
