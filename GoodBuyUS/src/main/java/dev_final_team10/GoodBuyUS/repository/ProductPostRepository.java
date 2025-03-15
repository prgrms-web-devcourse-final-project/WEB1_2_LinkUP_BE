package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {
   public List<ProductPost> findAllByAvailableIsFalse();
   public List<ProductPost> findAllByAvailableIsTrue();
   @Query("SELECT DISTINCT pp FROM ProductPost pp " +
           "JOIN FETCH pp.product p " +
           "LEFT JOIN FETCH p.reviews")
   List<ProductPost> findAllWithProductAndReviews();

   @Query("SELECT pp FROM ProductPost pp " +
           "JOIN FETCH pp.product p " +
           "LEFT JOIN FETCH p.reviews r " +
           "WHERE pp.postId = :id")
   ProductPost findDetailByPostId(Long id);

   @Modifying
   @Transactional
   @Query(value = "UPDATE product_post p SET p.product_period = DATE_ADD(p.product_period, INTERVAL 10 DAY), p.available = true", nativeQuery = true)
   void updateDate();
}
