package dev_final_team10.GoodBuyUS.dto.productpost;

import dev_final_team10.GoodBuyUS.domain.ProductPost;
import lombok.Data;

@Data
public class PostDetailDTO {
    private String name;
    private int originalprice;
    private int discountprice;
    private int minamount;
    private double rating;
    private String url;
    private String description;
    public static PostDetailDTO of(ProductPost productPost){
        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.name = productPost.getTitle();
        postDetailDTO.originalprice  = productPost.getOriginalPrice();
        postDetailDTO.discountprice = productPost.getProuctDiscount();
        postDetailDTO.minamount = productPost.getMinAmount();
        postDetailDTO.rating = productPost.getProduct().getAverageRating();
        postDetailDTO.url = productPost.getPostURL();
        postDetailDTO.description = productPost.getPostDescription();
        return postDetailDTO;
    }
}
