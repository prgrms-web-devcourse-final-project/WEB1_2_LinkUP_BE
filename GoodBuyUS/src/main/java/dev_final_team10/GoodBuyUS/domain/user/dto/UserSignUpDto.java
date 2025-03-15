package dev_final_team10.GoodBuyUS.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;

    //주소
    private String address;
}