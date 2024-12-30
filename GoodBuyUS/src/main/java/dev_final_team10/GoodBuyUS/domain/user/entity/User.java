package dev_final_team10.GoodBuyUS.domain.user.entity;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "user_reivews", joinColumns = @JoinColumn(name = "member_id"))
    private Set<Long> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

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

    public void changeProfile(String newProfile){
        this.profile = newProfile;
    }

    public void changeNickName(String nickname){
        this.nickname = nickname;
    }
}
