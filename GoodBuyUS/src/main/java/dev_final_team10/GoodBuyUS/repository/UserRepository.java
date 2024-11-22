package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
