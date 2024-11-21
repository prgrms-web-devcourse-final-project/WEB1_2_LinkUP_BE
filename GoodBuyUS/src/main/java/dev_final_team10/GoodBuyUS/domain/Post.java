package dev_final_team10.GoodBuyUS.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productDescription; // 상품 설명
    private int price; // 상품 가격
    private Boolean isPaid; // 결제 여부

    @OneToMany(mappedBy = "post")
    private List<ChatMember> purchases; // 구매자 목록

    public Post(String productName, String productDescription, int price) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.isPaid = false;  // 기본값은 결제 안된 상태
    }

    public void addPurchase(ChatMember chatMember) {
        this.purchases.add(chatMember);
    }
}
