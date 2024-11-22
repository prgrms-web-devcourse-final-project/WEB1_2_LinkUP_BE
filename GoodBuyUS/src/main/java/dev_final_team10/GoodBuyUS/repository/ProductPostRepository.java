package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {
}
