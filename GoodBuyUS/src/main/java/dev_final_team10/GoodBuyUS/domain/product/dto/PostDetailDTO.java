package dev_final_team10.GoodBuyUS.domain.product.dto;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDetailDTO {
    private String name;
    private int originalprice;
    private int discountprice;
    private int minamount;
    private double rating;
    private String url;
    private String description;
    private List<ReviewDTO> reviews;
    private Long productPostId;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private int currentStock;
    private boolean available;
//    private int now; 글 생성 시 currentStock을 프론트 저장공간에 뒀다가 currentstock을 빼서 주면 안되나요? 이거 팔린 개수를 짜는 로직이 너무 깁니다

    @Data
    public static class ReviewDTO{
        private Long reviewId;
        private String content;
        private int rating;
    }

    public static PostDetailDTO of(ProductPost productPost, List<ReviewDTO> reviews){
        return new PostDetailDTO(
                productPost.getTitle(),
                productPost.getOriginalPrice(),
                productPost.getProuctDiscount(),
                productPost.getMinAmount(),
                productPost.getProduct().getAverageRating(),
                productPost.getPostURL(),
                productPost.getPostDescription(),
                reviews,
                productPost.getPostId(),
                productPost.getCreatedAt(),
                productPost.getProduct_period(),
                productPost.getProduct().getStock(),
                productPost.isAvailable()
        );
    }
}
