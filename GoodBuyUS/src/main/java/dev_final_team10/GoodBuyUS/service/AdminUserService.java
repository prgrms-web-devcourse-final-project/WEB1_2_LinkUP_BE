package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 회원에게 경고 주기
    @Transactional
    public boolean warnUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        user.addWarning(); // 경고 수 증가
        if (user.getWarningCount() >= 3) { // 경고 3회 누적 시
            banUser(userId); // 회원 강제 탈퇴
            return true;
        }

        userRepository.save(user);
        return false;
    }

    // 회원 강제 탈퇴
    @Transactional
    public void banUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
