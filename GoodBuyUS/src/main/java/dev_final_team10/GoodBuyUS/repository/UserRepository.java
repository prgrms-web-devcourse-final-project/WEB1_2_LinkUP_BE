package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPhone(String phone);

    Optional<User> findByRefreshToken(String refreshToken);
}
