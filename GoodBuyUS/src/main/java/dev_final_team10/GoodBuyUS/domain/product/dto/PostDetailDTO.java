package dev_final_team10.GoodBuyUS.domain.product.dto;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Data
    public static class ReviewDTO{
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
                productPost.getPostId()
        );
    }
}
