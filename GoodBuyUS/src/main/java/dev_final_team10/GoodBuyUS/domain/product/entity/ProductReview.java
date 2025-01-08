package dev_final_team10.GoodBuyUS.domain.product.entity;

import dev_final_team10.GoodBuyUS.domain.BaseEntity;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ProductReview extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Min(value = 1, message = "최소 별점은 1점입니다.")
    @Max(value = 5, message = "최대 5점을 넘을 수 없습니다.")
    private int rating;

    @Column(nullable = false)
    private Long userId;

    private boolean isused;

    /**
     * 연관관계 편의 메서드
     * @param product
     */
    public void bindReview(Product product) {
        product.getReviews().add(this);
        this.product = product;
    }

    /**
     * @param product
     * @param content
     * @param rating
     * @return
     */
    public static ProductReview createProductReview(Product product, String content, int rating, Long userId) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null when creating a ProductReview.");
        }
        ProductReview productReview = new ProductReview();
        productReview.product = product;
        productReview.content = content;
        productReview.rating = rating;
        productReview.userId = userId;
        productReview.isused = true;
        return productReview;
    }

    public void changeContent(String content){
        this.content = content;
    }
    public void changeRating(int rating){
        this.rating = rating;
    }
    public void removeReview(){
        this.isused = false;
    }
}
