package dev_final_team10.GoodBuyUS.domain.product.entity;

import dev_final_team10.GoodBuyUS.domain.BaseEntity;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "min_amount")
    private int minAmount;

    //판매 기간
    private LocalDateTime product_period;

    private String title;
    /**
     * OneToOne은 항상 eager ?
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 물건 원가
    private int originalPrice;

    @OneToMany(mappedBy = "productPost")
    private List<Order> orders = new ArrayList<>();

    //구매 가능 여부
    private boolean available;
    public static ProductPost createProPost(Product product, String postDescription, int minAmount,LocalDateTime product_period){
        ProductPost productPost = new ProductPost();
        productPost.postDescription = postDescription;
        productPost.product = product;
        productPost.product_period = product_period;
        productPost.minAmount = minAmount;
        productPost.postURL = product.getProductImage();
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

    public void unAvailable(){
        this.available = false;
    }

    public void canSelling(){
        this.available = true;
    }

}
