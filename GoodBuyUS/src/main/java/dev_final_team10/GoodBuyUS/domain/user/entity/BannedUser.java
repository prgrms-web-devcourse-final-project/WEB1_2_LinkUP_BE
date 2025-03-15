package dev_final_team10.GoodBuyUS.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Table(name = "banned_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class BannedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 자동 증가되는 기본키

    private String name; // 이름
    private String email; // 이메일
    private String password; // 비밀번호
    private String phone; // 전화번호
    private String nickname; // 닉네임
    private String profile; // 프로필이미지
    private String address; // 주소

    @Column(name = "sns_type")
    private String snsType; // 소셜 로그인 타입 (예: "NAVER")

    @Column(name = "sns_id")
    private String snsId; // 소셜 로그인 사용자 고유 ID

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    private int warnings; // 경고 횟수
    private String reason; // 정지 사유
    private LocalDateTime banStart; // 정지된 시간
    private LocalDateTime banEnd; // 정지 해제 시간

    // 정지 상태 확인 메서드
    public boolean isBanActive() {
        return banEnd.isAfter(LocalDateTime.now());
    }
}
