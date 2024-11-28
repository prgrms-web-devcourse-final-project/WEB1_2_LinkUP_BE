package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {
   public List<ProductPost> findAllByAvailableIsFalse();
}
