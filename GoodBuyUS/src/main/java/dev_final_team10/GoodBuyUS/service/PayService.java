//package dev_final_team10.GoodBuyUS.service;
//
//import dev_final_team10.GoodBuyUS.domain.order.dto.TossRequestDTO;
//import dev_final_team10.GoodBuyUS.domain.order.entity.Delivery;
//import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
//import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
//import dev_final_team10.GoodBuyUS.domain.payment.dto.PaymentDTO;
//import dev_final_team10.GoodBuyUS.domain.payment.dto.RefundDTO;
//import dev_final_team10.GoodBuyUS.domain.payment.dto.RefundResponseDTO;
//import dev_final_team10.GoodBuyUS.domain.payment.entity.Payment;
//import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
//import dev_final_team10.GoodBuyUS.repository.OrderRepository;
//import dev_final_team10.GoodBuyUS.repository.PaymentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PayService {
//    private final PaymentRepository paymentRepository;
//    private final OrderRepository orderRepository;
//
//    public PaymentDTO paymentCheck(Order order, TossRequestDTO tossRequestDTO){
//        // 토스 api 연동해서 하는 결제 로직 가정, 실패의 경우
//        if (tossRequestDTO.getAmount() < 0){
//            // 결제 실패를 가정, 결제 중 잔액 부족으로 실패하거나 타임 아웃으로 인해 결제 취소되든 결제 중 결제가 취소되는 모든 경우
//            Payment payment = new Payment(PaymentStatus.FAIL, order);
//            paymentRepository.save(payment);
//            order.changeOrderStatus(OrderStatus.FAILED);
//            return new PaymentDTO(PaymentStatus.FAIL, "결제 실패");
//        }
//        else {Payment payment = new Payment(PaymentStatus.SUCCESS, order, tossRequestDTO.getAmount());
//            paymentRepository.save(payment);
//        }
//        return new PaymentDTO(PaymentStatus.SUCCESS, "토스 api key", tossRequestDTO.getOrderId(), tossRequestDTO.getAmount());
//    }
//
//    /**
//     * 토스에 환불 보내기
//     * @param refundDTO
//     * @return
//     */
//    public String refundPayment(RefundDTO refundDTO){
//        Order order  = orderRepository.findById(refundDTO.getOrderId()).orElseThrow(()->new RuntimeException("이건 안돼요"));
//        Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId());
//        //RefundDTO를 토스 api로 보냄, 환불이 취소되는 경우도 고려해야함
//        RefundResponseDTO response = new RefundResponseDTO("환불 완료");
//        payment.changeStatus(PaymentStatus.REFUND);
//        order.changeOrderStatus(OrderStatus.CANCEL);
//        order.defineDelivery(Delivery.REFUND);
//        /**
//         * 상태 업데이트, 만약 환불하는 시점에서 데드라인이 지나지 않았다면 물품은 다시 판매 가능해야함
//         */
//        // 데드라인이 지나거나 같음 -> 판매 불가
//        if (LocalDateTime.now().isAfter(order.getProductPost().getProduct_period())) {
//            order.getProductPost().getProduct().increaseStock(order.getQuantity());
//            order.getProductPost().unAvailable();
//        }
//        // 데드라인이 지나지 않음 -> 판매 가능
//        else {
//            order.getProductPost().getProduct().increaseStock(order.getQuantity());
//                order.getProductPost().canSelling();
//        }
//        return response.getMessage();
//    }
//}
