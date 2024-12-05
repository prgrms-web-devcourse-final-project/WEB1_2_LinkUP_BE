package dev_final_team10.GoodBuyUS.domain.user.dto;

import lombok.Data;

@Data
public class BanUserRequestDTO {
    private Long userId; // 정지할 회원 ID
    private int banDays; // 정지 기간 (일 단위)
    private String reason; // 정지 사유
}
