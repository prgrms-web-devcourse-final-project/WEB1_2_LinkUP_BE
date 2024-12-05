package dev_final_team10.GoodBuyUS.domain.community.entity;

import lombok.Getter;

@Getter
public enum participationStatus {
    JOIN("참여"),
    CANCEL("참여취소"),
    PAYMENT_STANDBY("결제대기"),
    PAYMENT_CANCLE("결제취소"),
    PAYMENT_COMPELETE("결제완료");

    private String statusName;
    participationStatus(String statusName) {
        this.statusName = statusName;
    }
}
