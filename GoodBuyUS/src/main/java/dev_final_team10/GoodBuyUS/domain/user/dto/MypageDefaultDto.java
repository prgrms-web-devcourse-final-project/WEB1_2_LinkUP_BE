package dev_final_team10.GoodBuyUS.domain.user.dto;

import dev_final_team10.GoodBuyUS.domain.order.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class MypageDefaultDto {
    private String name;
    private String profile;
    private String phoneNum;
    private String address;
    private String nickname;
}
