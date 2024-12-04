package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 조회
    Optional<User> findByNickname(String nickname);

    // 전화번호로 사용자 조회
    Optional<User> findByPhone(String phone);

    // RefreshToken으로 사용자 조회
    Optional<User> findByRefreshToken(String refreshToken);

    // 네이버 snsId로 사용자 조회
    Optional<User> findBySnsId(String snsId);

    // 소셜 로그인 타입과 snsId로 사용자 조회
    Optional<User> findBySnsTypeAndSnsId(String snsType, String snsId);
}
