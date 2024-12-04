package dev_final_team10.GoodBuyUS.product;

import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.product.category.SubCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("상품의 카테고리가 올바르게 설정되는지 테스트")
public class CreateProduct {

    @Test
    @DisplayName("상단 상품 - 리뷰 리스트 연결 확인")
    public void create(){
        Product product = Product.createProduct("유아용 바지",10000, "qqwe",ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS);
        Assertions.assertThat(product.getReviews().size()).isEqualTo(0);
    }
}
