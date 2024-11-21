package dev_final_team10.GoodBuyUS.product;

import dev_final_team10.GoodBuyUS.domain.Product;
import dev_final_team10.GoodBuyUS.domain.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.category.SubCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("상품의 카테고리가 올바르게 설정되는지 테스트")
public class CreateProduct {

    @Test
    @DisplayName("카테고리 - 티셔츠 테스트")
    public void create(){
        Product product =   new Product(1L,"진짜 멋진 옷", 100000, "사진 이미지", ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS);
        Assertions.assertThat(product.getProductCategory()).isEqualTo(ProductCategory.FASHION);
    }

}
