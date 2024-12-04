package dev_final_team10.GoodBuyUS.controller;
import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ReviewRequestDTO;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goodbuyUs")
@RequiredArgsConstructor
public class ProductPostController {
    private final ProductPostService productPostService;

    @GetMapping()
    public List<ProductPostDTO> getPosts(){
       return productPostService.findAllProduct();
    }

    @GetMapping("/product")
    public PostDetailDTO detailProduct(@RequestParam Long postid){
        return productPostService.findPost(postid);
    }

    // 리뷰 생성
    @PostMapping("/product/{post_id}")
    public HttpEntity<?> createReview(@RequestHeader("Authorization") String token, @PathVariable Long post_id, @RequestBody ReviewRequestDTO reviewRequestDTO){
        String userEmail = extractEmailFromToken(token);
        return productPostService.addReview(userEmail,reviewRequestDTO,post_id);
    }

    // 리뷰 삭제 (햄버거로 삭제)
    @DeleteMapping("/product/{review_id}")
    public HttpEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable Long review_id){
        String userEmail = extractEmailFromToken(token);
        return productPostService.deleteReview(userEmail,review_id);
    }

    // 리뷰 수정
    @PutMapping("/product/{review_id}")
    public HttpEntity<?> updateReview(@RequestHeader("Authorization") String token, @PathVariable Long review_id, @RequestBody ReviewRequestDTO reviewRequestDTO){
        String userEmail = extractEmailFromToken(token);
        return productPostService.updateReview(userEmail,reviewRequestDTO,review_id);
    }

    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }
}
