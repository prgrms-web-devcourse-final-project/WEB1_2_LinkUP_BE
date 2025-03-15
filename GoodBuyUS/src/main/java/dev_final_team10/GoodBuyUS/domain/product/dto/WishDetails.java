package dev_final_team10.GoodBuyUS.domain.product.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class WishDetails {
    private Long productPostId;
    private String productName;
    private int productPrice;
    private String productImage;
    private LocalDateTime addedAt;

    public WishDetails(Long productPostId, String productName, int productPrice, String productImage, LocalDateTime addedAt) {
        this.productPostId = productPostId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.addedAt = addedAt;
    }
}
