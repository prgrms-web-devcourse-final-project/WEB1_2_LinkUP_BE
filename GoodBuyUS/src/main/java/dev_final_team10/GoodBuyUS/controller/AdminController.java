package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserService adminUserService;

    // 모든 회원 목록 조회 (관리자 권한만 접근 가능)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 회원 경고 주기
    @PostMapping("/warn/{userId}")
    public ResponseEntity<String> warnUser(@PathVariable Long userId) {
        boolean isBanned = adminUserService.warnUser(userId);
        if (isBanned) {
            return ResponseEntity.ok("해당 회원은 경고 누적으로 인해 강제 탈퇴되었습니다.");
        }
        return ResponseEntity.ok("회원에게 경고를 부여했습니다.");
    }

    // 회원 강제 탈퇴
    @DeleteMapping("/ban/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Long userId) {
        adminUserService.banUser(userId);
        return ResponseEntity.ok("회원이 강제 탈퇴되었습니다.");
    }
}
