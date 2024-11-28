package dev_final_team10.GoodBuyUS.domain.order.entity;

public enum OrderStatus {
    CANCEL, COMPLETE, WAITING, FAILED
    //환불의 경우 cancel, waiting은 입금 후 관리자(판매자)의 주문 완료가 떨어지기 전, FAILED는 결제 실패의 경우, complete는 주문완료
}
