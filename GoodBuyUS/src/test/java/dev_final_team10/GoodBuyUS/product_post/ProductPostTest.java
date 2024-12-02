package dev_final_team10.GoodBuyUS.product_post;

import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("test")
public class ProductPostTest {
    @Autowired
    ProductPostRepository productPostRepository;
    @Autowired
    ProductRepository productRepository;

    @Test @DisplayName("게시글 생성 테스트")
    public void createPost(){
        Product product = productRepository.findById(1L).orElseThrow(()->new NoSuchElementException("없는 아이디입니다"));
        ProductPost productPost = ProductPost.createProPost(product, "이건 진짜 좋은 상품입니다", 2, LocalDate.now().plusDays(3),100);
        productPostRepository.save(productPost);
        Assertions.assertThat(productPost.getPostURL()).isEqualTo(product.getProductImage());
        Assertions.assertThat(productPost.getStockQuantity()).isEqualTo(100);
    }

    @Test @DisplayName("할인 검증 테스트, 할인률이 높아 가격이 0보다 작을 때")
    public void discount(){
        Product product = productRepository.findById(1L).orElseThrow(()->new NoSuchElementException("없는 아이디입니다"));
        ProductPost productPost = ProductPost.createProPost(product, "이건 진짜 좋은 상품입니다", 11, LocalDate.now().plusDays(3),100);
        productPostRepository.save(productPost);
        productPost.setOriginalandDiscount();
        Assertions.assertThat(productPost.isAvailable()).isEqualTo(false);
    }

    @Test @DisplayName("주문 구매 후 취소 시 수량 확인")
    public void cancel(){
        Product product = productRepository.findById(1L).orElseThrow(()->new NoSuchElementException("없는 아이디입니다"));
        ProductPost productPost = ProductPost.createProPost(product, "이건 진짜 좋은 상품입니다", 2, LocalDate.now().plusDays(3),100);
        productPostRepository.save(productPost);
        productPost.setOriginalandDiscount();
        productPost.purchaseProduct(20);
        productPost.cancelPurchase(10);
        Assertions.assertThat(productPost.getStockQuantity()).isEqualTo(90);
    }
}
