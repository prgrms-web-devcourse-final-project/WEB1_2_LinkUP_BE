package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductReview;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
   public Optional<ProductReview> findByUserIdAndProductReviewId(Long userid, Long reviewId);
   @Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.product = :product AND pr.isused = true")
   public double getRate(Product product);
}
