package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.user.dto.BanUserRequestDTO;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 모든 회원 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 회원 경고 및 정지
    @PostMapping("/warn")
    public ResponseEntity<String> warnAndBanUser(@RequestBody BanUserRequestDTO dto) {
        adminUserService.warnAndBanUser(dto);
        return ResponseEntity.ok("회원이 경고 및 정지되었습니다.");
    }

    // 회원 정지 해제
    @PostMapping("/unban/{bannedUserId}")
    public ResponseEntity<String> unbanUser(@PathVariable Long bannedUserId) {
        adminUserService.unbanUser(bannedUserId);
        return ResponseEntity.ok("회원 정지가 해제되었습니다.");
    }
}
