package dev_final_team10.GoodBuyUS.review;

import dev_final_team10.GoodBuyUS.domain.Product;
import dev_final_team10.GoodBuyUS.domain.ProductReview;
import dev_final_team10.GoodBuyUS.domain.category.DetailCategory;
import dev_final_team10.GoodBuyUS.domain.category.ProductCategory;
import dev_final_team10.GoodBuyUS.domain.category.SubCategory;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import dev_final_team10.GoodBuyUS.repository.ProductReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CreateReview {
    @Autowired
    ProductRepository productRepository ;
    @Autowired
    ProductReviewRepository productReviewRepository;
    @Test
    @DisplayName("리뷰 생성 테스트")
    public void Review(){
        Product product = Product.createProduct("유아용 바지",10000, "qqwe",ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS);
        productRepository.save(product);
        /**
         * productReview에 product는 프록시 상태 ?
         */
//        ProductReview productReview = ProductReview.createProductReview(product,"이거 예뻐요",5);
//        productReviewRepository.save(productReview);
//        Assertions.assertThat(productReview.getProduct().getProductId()).isEqualTo(1L);
    }
    @Test
    @DisplayName("리뷰 생성 시간 테스트")
    public void createdReviewTime(){
        Product product = Product.createProduct("유아용 바지",10000, "qqwe",ProductCategory.FASHION, SubCategory.CHILDREN_CLOTHING, DetailCategory.KIDS_CLOTHING_PANTS);
        productRepository.save(product);
//        ProductReview productReview = ProductReview.createProductReview(product,"이거 예뻐요",5);
//        productReviewRepository.save(productReview);
//        Assertions.assertThat(productReview.getCreatedAt()).isNotEqualTo(LocalDateTime.now());
    }
}
