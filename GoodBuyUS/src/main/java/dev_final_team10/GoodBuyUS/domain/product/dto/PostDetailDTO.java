package dev_final_team10.GoodBuyUS.domain.product.dto;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int initstock;

    @Data
    public static class ReviewDTO{
        private Long reviewId;
        private String content;
        private int rating;
        private boolean using;
    }

    public static PostDetailDTO of(ProductPost productPost, List<ReviewDTO> reviews, double rating){
        return new PostDetailDTO(
                productPost.getTitle(),
                productPost.getOriginalPrice(),
                productPost.getProuctDiscount(),
                productPost.getMinAmount(),
                rating,
                productPost.getPostURL(),
                productPost.getPostDescription(),
                reviews,
                productPost.getPostId(),
                productPost.getCreatedAt(),
                productPost.getProduct_period(),
                productPost.getProduct().getStock(),
                productPost.isAvailable(),
                productPost.getInitstock()
        );
    }
}
