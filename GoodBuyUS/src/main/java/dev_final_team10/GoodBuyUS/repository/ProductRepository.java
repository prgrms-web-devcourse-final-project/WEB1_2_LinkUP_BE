package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
