package dev_final_team10.GoodBuyUS.controller.api;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.product.dto.ReviewRequestDTO;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ProductPostService productPostService;
    // 리뷰 생성
    @PostMapping("/{post_id}")
    public HttpEntity<?> createReview(@RequestHeader("Authorization") String token, @PathVariable Long post_id, @RequestBody ReviewRequestDTO reviewRequestDTO){
        String userEmail = extractEmailFromToken(token);
        return productPostService.addReview(userEmail,reviewRequestDTO,post_id);
    }

    // 리뷰 삭제 (햄버거로 삭제)
    @PutMapping("/remove/{review_id}")
    public HttpEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable Long review_id){
        String userEmail = extractEmailFromToken(token);
        return productPostService.deleteReview(userEmail,review_id);
    }

    // 리뷰 물리적 삭제

    /**
     * 유저에서 관리하는 리뷰 테이블에서 안지워짐;; 이건 어떻게 같이 지우지?
     * @param token
     * @param review_id
     */
    @DeleteMapping("/remove/{review_id}")
    public void deleteReview2(@RequestHeader("Authorization") String token, @PathVariable Long review_id){
        String userEmail = extractEmailFromToken(token);
        productPostService.deleteReview(review_id, userEmail);
    }

    // 리뷰 수정
    @PutMapping("/update/{review_id}")
    public HttpEntity<String> updateReview(@RequestHeader("Authorization") String token, @PathVariable Long review_id, @RequestBody ReviewRequestDTO reviewRequestDTO){
        String userEmail = extractEmailFromToken(token);
        return productPostService.updateReview(userEmail,reviewRequestDTO,review_id);
    }

    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }
}
