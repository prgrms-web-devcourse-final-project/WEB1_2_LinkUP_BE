package dev_final_team10.GoodBuyUS.domain;

import dev_final_team10.GoodBuyUS.domain.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.category.SubCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private int productPrice;
    private String productImage;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    @Enumerated(EnumType.STRING)
    private DetailCategory detailCategory;
}
