package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.user.dto.BanUserRequestDTO;
import dev_final_team10.GoodBuyUS.domain.user.entity.BannedUser;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.BannedUserRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final BannedUserRepository bannedUserRepository;

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 회원 경고 및 정지
    @Transactional
    public void warnAndBanUser(BanUserRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        // 경고 수 증가
        user.addWarning();
        userRepository.save(user);

        // 정지 로직
        if (dto.getBanDays() > 0) { // 정지 기간이 명시된 경우
            BannedUser bannedUser = BannedUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .profile(user.getProfile())
                    .snsType(user.getSnsType())
                    .snsId(user.getSnsId())
                    .reason(dto.getReason())
                    .banStart(LocalDateTime.now())
                    .banEnd(LocalDateTime.now().plusDays(dto.getBanDays()))
                    .build();

            bannedUserRepository.save(bannedUser);
            userRepository.delete(user); // User 엔티티에서 제거
        }
    }

    // 정지 해제
    @Transactional
    public void unbanUser(Long bannedUserId) {
        BannedUser bannedUser = bannedUserRepository.findById(bannedUserId)
                .orElseThrow(() -> new RuntimeException("해당 정지된 회원이 존재하지 않습니다."));

        // 정지 해제된 회원을 다시 User 엔티티로 복구
        User restoredUser = User.builder()
                .id(bannedUser.getId())
                .name(bannedUser.getName())
                .email(bannedUser.getEmail())
                .phone(bannedUser.getPhone())
                .address(bannedUser.getAddress())
                .profile(bannedUser.getProfile())
                .snsType(bannedUser.getSnsType())
                .snsId(bannedUser.getSnsId())
                .role(bannedUser.getRole())
                .build();

        userRepository.save(restoredUser);
        bannedUserRepository.delete(bannedUser); // BannedUser 엔티티에서 제거


    }
    @Transactional
    @Scheduled (cron = "0 0 */1 * * *")// 매 1분마다 실행
    public void autoUnbanExpiredUsers() {
        List<BannedUser> expiredBans = bannedUserRepository.findAll().stream()
                .filter(bannedUser -> bannedUser.getBanEnd().isBefore(LocalDateTime.now()))
                .toList();

        for (BannedUser bannedUser : expiredBans) {
            unbanUser(bannedUser.getId());
        }
    }
}
