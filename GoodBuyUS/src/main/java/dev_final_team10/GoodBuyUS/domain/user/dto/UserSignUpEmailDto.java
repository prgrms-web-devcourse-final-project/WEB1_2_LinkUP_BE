package dev_final_team10.GoodBuyUS.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpEmailDto {
    private String email;
    private String password;
    private String name;
    private String phone;
}
