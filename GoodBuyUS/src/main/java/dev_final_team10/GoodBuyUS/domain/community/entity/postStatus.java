package dev_final_team10.GoodBuyUS.domain.community.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum postStatus {
    NOT_APPROVED("승인대기"),
    APPROVED("승인완료");

    private final String categoryName;
    postStatus(String categoryName) {
        this.categoryName = categoryName;
    }
}
