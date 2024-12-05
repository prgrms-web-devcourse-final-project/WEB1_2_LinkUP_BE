package dev_final_team10.GoodBuyUS.domain.product.entity;

import dev_final_team10.GoodBuyUS.domain.product.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.SubCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ProductReview> reviews = new ArrayList<>();

    private int reviewsCount;

    private double averageRating;

    private int stock;

    public static Product createProduct(String productName, int productPrice, String productImage, ProductCategory productCategory, SubCategory subCategory, DetailCategory detailCategory, int stock) {
        Product product = new Product();
        product.productName = productName;
        product.productImage = productImage;
        product.productCategory = productCategory;
        product.detailCategory = detailCategory;
        product.subCategory = subCategory;
        product.productPrice = productPrice;
        product.stock = stock;
        product.averageRating = 0.0;
        return product;
    }

    public void decreaseStock(int count){
        this.stock -= count;
    }

    public void increaseStock(int count){
        this.stock += count;
    }
}
