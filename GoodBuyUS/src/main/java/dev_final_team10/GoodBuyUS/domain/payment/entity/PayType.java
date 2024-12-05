package dev_final_team10.GoodBuyUS.domain.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌");

    private final String name;
}
// 상단계좌에서 어떤식으로 쓸지 고민 중