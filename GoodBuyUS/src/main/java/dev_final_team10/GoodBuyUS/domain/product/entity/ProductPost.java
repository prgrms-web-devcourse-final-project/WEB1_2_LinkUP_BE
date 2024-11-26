package dev_final_team10.GoodBuyUS.domain.product.entity;

import dev_final_team10.GoodBuyUS.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ProductPost extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "post_id")
    private Long postId;

    // 할인이 적용된 최종 판매가격
    private int prouctDiscount;

    private String postDescription;

    private String postURL;

    //최소 개수(2명으로 고정)
    @Column(name = "min_amount")
    private int minAmount;

    //판매 기간
    private LocalDate product_period;

    private String title;
    /**
     * OneToOne은 항상 eager ?
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 물건 원가
    private int originalPrice;

    // 판매 재고
    private int stockQuantity;

    //구매 가능 여부
    private boolean available;
    public static ProductPost createProPost(Product product, String postDescription, int minAmount,LocalDate product_period, int stockQuantity){
        ProductPost productPost = new ProductPost();
        productPost.postDescription = postDescription;
        productPost.product = product;
        productPost.product_period = product_period;
        productPost.minAmount = minAmount;
        productPost.postURL = product.getProductImage();
        productPost.stockQuantity = stockQuantity;
        productPost.available = true;
        productPost.originalPrice = product.getProductPrice();
        productPost.setOriginalandDiscount();
        productPost.title = product.getProductName();
        return productPost;
    }

    /**
     * 만약 해당 글의 최소 참여자가 2명이라고 가정했을 때 최대 할인율은 1000원
     * 즉 원가를 product에서 가져와서 표시하고 최종 구매가를 계산해서 discount에 넣어주면 됨
     */
    public void setOriginalandDiscount(){
        if(this.originalPrice - (this.minAmount * 500) > 0){
            this.prouctDiscount = this.originalPrice - (this.minAmount * 500);
        }
        else {
            this.prouctDiscount = -1;
            this.available = false;
        }
    }

    /**
     * 동시성 이슈는 고려하지 않음(고려해야함 ㅠㅠ)
     * @param count
     */
    public void purchaseProduct(int count){
        if (count <= 0) {
            throw new IllegalArgumentException("구매 수량은 1 이상이어야 합니다.");
        }
        if (this.stockQuantity < count) {
            this.available = false;
            throw new IllegalStateException("재고가 부족합니다. 남은 재고: " + this.stockQuantity);
        }
        this.stockQuantity -= count;
    }

    public void cancelPurchase(int count){
        if (count <= 0) {
            throw new IllegalArgumentException("취소 수량은 1 이상이어야 합니다.");
        }
        this.stockQuantity += count;
    }

    /**
     * 글 마다 재고를 두고 그 재고가 줄어드는 것을 가시적으로 표시
     * 데드라인과 재고 안에선 무제한 구매 가능
     * 최소 인원을 정해두고 그 인원이 넘어서면
     */
}
