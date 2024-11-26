package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
}
