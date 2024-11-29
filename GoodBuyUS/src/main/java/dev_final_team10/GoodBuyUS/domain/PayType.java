package dev_final_team10.GoodBuyUS.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌");

    private final String name;
}