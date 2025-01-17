package dev_final_team10.GoodBuyUS.domain.product.dto;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.product.category.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductPostDTO {
    private Long id;
    private String name;
    private int originalprice;
    private int discountprice;
    private int minamount;
    private double rating;
    private String url;
    private LocalDateTime deadline;
//    private boolean selling;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private boolean available;
    public static ProductPostDTO of(ProductPost productPost, double rating){
        ProductPostDTO productPostDTO = new ProductPostDTO();
        productPostDTO.id = productPost.getPostId();
        productPostDTO.name = productPost.getTitle();
        productPostDTO.originalprice  = productPost.getOriginalPrice();
        productPostDTO.discountprice = productPost.getProuctDiscount();
        productPostDTO.minamount = productPost.getMinAmount();
        productPostDTO.rating = rating;
        productPostDTO.category = productPost.getProduct().getProductCategory(); //n+1문제 발생
        productPostDTO.available = productPost.isAvailable();
        productPostDTO.deadline = productPost.getProduct_period();
        return productPostDTO;
    }
}
