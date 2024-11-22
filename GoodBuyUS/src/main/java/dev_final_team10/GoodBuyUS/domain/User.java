package dev_final_team10.GoodBuyUS.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private String phone;
    private String nickname;
    private String profile;
    private String refreshToken;
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ProductReview> productReviewLists = new ArrayList<>();

    public User(String name, Role role, String email, String password, String phone, String nickname, String profile) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.profile = profile;
    }
}
