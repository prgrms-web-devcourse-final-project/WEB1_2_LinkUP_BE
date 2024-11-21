package dev_final_team10.GoodBuyUS.domain;

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
public class ProductReview extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productReviewId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Min(value = 1, message = "최소 별점은 1점입니다.")
    @Max(value = 5, message = "최대 5점을 넘을 수 없습니다.")
    private int rating;

    /**
     * 연관관계 편의 메서드
     * @param product
     */
    public void bindReview(Product product) {
        product.getReviews().add(this);
        this.product = product;
    }

    /**
     * Product가 없을 때 리뷰를 생성하면 안됨, 리뷰를 생성할 때 product가 있는지 검증이 필요함
     * @param product
     * @param content
     * @param rating
     * @return
     */
    public static ProductReview createProductReview(Product product, String content, int rating){
        ProductReview productReview = new ProductReview();
        productReview.product = product;
        productReview.content = content;
        productReview.rating = rating;
        return productReview;
    }
}
