package dev_final_team10.GoodBuyUS.dto.productpost;

import dev_final_team10.GoodBuyUS.domain.ProductPost;
import dev_final_team10.GoodBuyUS.domain.category.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ProductPostDTO {
    private String name;
    private int originalprice;
    private int discountprice;
    private int minamount;
    private double rating;
    private String url;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    public static ProductPostDTO of(ProductPost productPost){
        ProductPostDTO productPostDTO = new ProductPostDTO();
        productPostDTO.name = productPost.getTitle();
        productPostDTO.originalprice  = productPost.getOriginalPrice();
        productPostDTO.discountprice = productPost.getProuctDiscount();
        productPostDTO.minamount = productPost.getMinAmount();
        productPostDTO.rating = productPost.getProduct().getAverageRating();
        productPostDTO.url = productPost.getPostURL();
        productPostDTO.category = productPost.getProduct().getProductCategory();
        return productPostDTO;
    }
}
