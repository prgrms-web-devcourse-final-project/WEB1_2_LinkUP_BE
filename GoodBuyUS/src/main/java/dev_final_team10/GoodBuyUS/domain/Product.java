package dev_final_team10.GoodBuyUS.domain;

import dev_final_team10.GoodBuyUS.domain.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.category.SubCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int productPrice;
    @Column(nullable = true)
    private String productImage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubCategory subCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetailCategory detailCategory;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews = new ArrayList<>();

    private int reviewscount;

    public static Product createProduct(String productName, int productPrice, String productImage, ProductCategory productCategory, SubCategory subCategory, DetailCategory detailCategory) {
        Product product = new Product();
        product.productName = productName;
        product.productImage = productImage;
        product.productCategory = productCategory;
        product.detailCategory = detailCategory;
        product.subCategory = subCategory;
        product.productPrice = productPrice;
        return product;
    }

    public void AmountOfReviews(){
        this.reviewscount = reviews.size();
    }
}
