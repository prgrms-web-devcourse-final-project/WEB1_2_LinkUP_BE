package dev_final_team10.GoodBuyUS.domain.community.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum postStatus {
    NOT_APPROVED("승인대기"),       //글 작성 후 관리자 승인 대기 중인 상태 (커뮤니티에서는 안 보임)
    APPROVED("승인완료"),           //승인이 완료되고 커뮤니티에서 모집 중인 상태 [참여하기] or [취소하기]
    COMPLETED("모집완료"),          //모집이 완료되었지만 글 생성시간과 글 수정시간이 다를 경우 - 관리자 승인이 필요 (관리자 승인 완료되면 결제대기로)
    PAYMENT_STANDBY("결제 대기"),   //모집이 완료되었고, 글 생성시간 = 글 수정시간일 경우 바로 결제대기 (12시간 카운트 다운) [결제하기][취소하기] or [환불하기]
    PAYMENT_COMPLETED("결제 완료"); //모두가 결제를 완료한 상태

    private final String categoryName;
    postStatus(String categoryName) {
        this.categoryName = categoryName;
    }
}
