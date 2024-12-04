package dev_final_team10.GoodBuyUS.domain.payment.entity;

public enum PaymentStatus {
    PENDING, // 결제 대기 중
    AUTH_COMPLETED, //결제 요청 인증 성공
    DONE,// 결제 완료
    FAILED, //결제 실패
    CANCELED,// 결제 취소(전체 취소, 전체 환불)
    PARTIAL_CANCELED, //부분 취소, 부분 환불)
    SUCCESS,
    FAIL,
    REFUND
}
