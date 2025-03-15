package dev_final_team10.GoodBuyUS.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

/**
 * 컬렉션을 따로 빼서 저장하는 위시리스트에 Set에서 컬렉션 형식으로 지정하면 여러개의 컬럼을 쓸 수 있음
 */
@Embeddable
public class WishListItem {

    @Column(name = "product_id", nullable = false)
    private Long productId;  // 기존 상품 ID

    @Column(name = "added_at", nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addedAt;  // 추가한 날짜

    // 기본 생성자 (JPA 필수)
    protected WishListItem() {}

    public WishListItem(Long productId) {
        this.productId = productId;
        this.addedAt = LocalDateTime.now(); // 현재 시간으로 저장
    }

    public Long getProductId() {
        return productId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}
