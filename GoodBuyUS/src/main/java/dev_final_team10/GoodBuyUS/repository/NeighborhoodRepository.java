package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.neighborhood.entity.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Double> {
    Neighborhood findByNeighborhoodName(String neighborhoodName);
}
