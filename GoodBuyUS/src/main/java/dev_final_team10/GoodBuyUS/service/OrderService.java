package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.Payment;
import dev_final_team10.GoodBuyUS.domain.payment.PaymentDTO;
import dev_final_team10.GoodBuyUS.domain.payment.PaymentStatus;
import dev_final_team10.GoodBuyUS.domain.payment.RefundDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.DetailProductDTo;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.PaymentRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductPostRepository productPostRepository;
    private final UserRepository userRepository;
    private final PayService payService;
    private final PaymentRepository paymentRepository;

    // 주문을 위해 이전 페이지에서 결정한 수량과 가격을 업데이트
    public DetailProductDTo readyToOrder(CountRequestDTO orderRequestDTO, Long postId){
        ProductPost productPost = productPostRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
        if(productPost.isAvailable() == false){
            // 예외 발생, 재고가 다 떨어졌거나 가격이 0보다 작아서 판매를 못하는 경우
        }
        if(orderRequestDTO.getAmount() > productPost.getProduct().getStock()){
            // 예외 발생, 주문 수량은 재고보다 클 수 없음
        }
        return DetailProductDTo.createDTO(productPost, orderRequestDTO.getAmount());
    }

    /**
     * 배송정보, 요청 사항을 통해 order를 생성하고 저장
     * @param orderRequestDTO
     * @param userEmail
     * @param postId
     * @return
     */
    public TossRequestDTO createOrder(OrderRequestDTO orderRequestDTO, String userEmail, Long postId){
        ProductPost productPost = productPostRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchElementException("없는 회원입니다"));
        Order order = Order.createOrder(productPost, user, orderRequestDTO.getQuantity(), orderRequestDTO.getDeliveryRequestDTO().getAddress(),
                orderRequestDTO.getPrice(),orderRequestDTO.getDeliveryRequestDTO().getNeeded());
        order.registUser(user);
        orderRepository.save(order);
        return new TossRequestDTO(order.getOrderId(),user.getId(),productPost.getPostId(),orderRequestDTO.getPrice(), orderRequestDTO.getQuantity());
    }

    /**
     * 토스 api를 통해 결제를 진행
     * @param tossRequestDTO
     * @return
     */
    public PaymentDTO payment(TossRequestDTO tossRequestDTO){
        Order order = orderRepository.findById(tossRequestDTO.getOrderId()).orElseThrow(()->new NoSuchElementException("없는 오더"));
        ProductPost post = productPostRepository.findById(tossRequestDTO.getPostId()).orElseThrow(()->new NoSuchElementException("없는 게시글"));
        Product product = post.getProduct();

        PaymentDTO paymentDTO = payService.paymentCheck(order,tossRequestDTO);

        //결제 성공, 성공 url로 리다이렉트
        if (paymentDTO.getStatus() == PaymentStatus.SUCCESS){
            // 배송이 끝나야 complete로 전환
            order.changeOrderStatus(OrderStatus.WAITING);
            product.decreaseStock(tossRequestDTO.getQuantity());
            return paymentDTO;
        }
        //결제 실패, 실패 url로 리다이렉트
        else order.changeOrderStatus(OrderStatus.FAILED);
        return paymentDTO;
    }

    /**
     * 주문 내역 조회
     */
    public List<OrdersDTO> findUsersOrderList(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NoSuchElementException("에러"));
        List<Order> orders = orderRepository.findOrderByUser(user);
        List<OrdersDTO> ordersDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrdersDTO ordersDTO = new OrdersDTO();
            Payment payment = paymentRepository.findByOrder(order);
            ordersDTO.setProductName(order.getOrderName());
            ordersDTO.setPirce(order.getPrice());
            ordersDTO.setOrderDate(order.getCreatedAt());
            ordersDTO.setPaymentStatus(payment.getPaymentStatus());
            ordersDTO.setPaymentId(payment.getPaymentId());
            ordersDTOS.add(ordersDTO);
        }
        return ordersDTOS;
    }

    /**
     * 사용자 환불
     **/
    public String refund(Long payId){
        Payment payment = paymentRepository.findById(payId).orElseThrow(()-> new NoSuchElementException("잘못된 오더"));
        Order order = orderRepository.findById(payment.getOrder().getOrderId()).orElseThrow();
        RefundDTO refundDTO = new RefundDTO(order.getOrderId(),payment.getPrice(),LocalDateTime.now());
        return payService.refundPayment(refundDTO);
    }

    /**
     * 관리자 환불
     * 데드라인이 지났음에도 불구하고, 할인 가격인 최소 수량에 맞춰 주문이 이루어지지 않은 경우
     * 1. 스케쥴러로 한시간마다 글 검증, 판매불가인 경우 구매 비활성화 전달
     */


}

