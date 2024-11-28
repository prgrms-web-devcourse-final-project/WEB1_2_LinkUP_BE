package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.order.dto.TossRequestDTO;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.*;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PayService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentDTO paymentCheck(Order order,TossRequestDTO tossRequestDTO){
        // 토스 api 연동해서 하는 결제 로직 가정
        if (tossRequestDTO.getAmount() < 0){
            Payment payment = new Payment(PaymentStatus.FAIL, order);
            paymentRepository.save(payment);
            return new PaymentDTO(PaymentStatus.FAIL, "결제 실패");
        }
        else {Payment payment = new Payment(PaymentStatus.SUCCESS, order, tossRequestDTO.getAmount());
            paymentRepository.save(payment);
        }
        return new PaymentDTO(PaymentStatus.SUCCESS, "토스 api key", tossRequestDTO.getOrderId(), tossRequestDTO.getAmount());
    }

    /**
     * 토스에 환불 보내기
     * @param refundDTO
     * @return
     */
    public String refundPayment(RefundDTO refundDTO){
        Order order  = orderRepository.findById(refundDTO.getOrderId()).orElseThrow(()->new RuntimeException("이건 안돼요"));
        Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId());
        //RefundDTO를 토스 api로 보냄, 환불이 취소되는 경우도 고려해야함
        RefundResponseDTO response = new RefundResponseDTO("환불 완료");

        // + 환불이 취소되는 경우
        payment.changeStatus(PaymentStatus.REFUND);
        order.changeOrderStatus(OrderStatus.CANCEL);
        order.getProductPost().getProduct().increaseStock(order.getQuantity());
        return response.getMessage();
    }
}
