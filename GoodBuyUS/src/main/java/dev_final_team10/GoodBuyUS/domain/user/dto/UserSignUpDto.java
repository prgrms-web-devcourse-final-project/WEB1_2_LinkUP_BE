package dev_final_team10.GoodBuyUS.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private String profile;
}
