package dev_final_team10.GoodBuyUS.domain.order.entity;

public enum OrderStatus {
    // 배송 완료 이후 환불 시 주문 상태
    CANCEL,
    // 배송 완료, 구매 확정, 구매 확정이면 앞으로 더 상태 변경 불가능, refund에서 상태 검증을 하면 좋을 것 같음
    COMPLETE,
    // 주문 대기, 주문이 만들어지고 성공 시 10초 후 배송 완료, 실패시 결제 취소로 분기
    WAITING,
    // 결제 실패, 토스에서 결제 중 사용자가 결제 창을 닫는다거나, 오래동안 결제창을 안 닫을 때 TIME OUT
    FAILED,

    //환불의 경우 cancel, waiting은 입금 후 관리자(판매자)의 주문 완료가 떨어지기 전, FAILED는 결제 실패의 경우, complete는 주문완료
}
