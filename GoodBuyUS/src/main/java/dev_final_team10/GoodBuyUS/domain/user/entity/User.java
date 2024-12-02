package dev_final_team10.GoodBuyUS.domain.user.entity;

import dev_final_team10.GoodBuyUS.domain.neighborhood.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductReview;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    private String refreshToken; // 리프레쉬 토큰
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    private String refreshToken;   //리프레쉬 토큰

    @Column(name = "warnings", nullable = false, columnDefinition = "int default 0")
    private int warnings; // 경고 횟수 (기본값 0)

    // 경고 횟수 반환 메서드
    public int getWarningCount() {
        return this.warnings;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_code")
    private Neighborhood neighborhood;   //지역코드


    //비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 재발급된 RefreshToke 저장 메소드
    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    // 경고 횟수 증가 메소드
    public void addWarning() {
        this.warnings++;
    }

    // 경고 횟수 초기화 메소드
    public void resetWarnings() {
        this.warnings = 0;
    }

    // 유저 자동 탈퇴 여부 확인 메소드
    public boolean shouldBeBanned() {
        return this.warnings >= 3; // 경고 3회 초과 시 탈퇴
    }

    // 소셜 로그인 사용자 체크 메소드
    public boolean isSocialUser() {
        return this.snsType != null && this.snsId != null;
    }
}
