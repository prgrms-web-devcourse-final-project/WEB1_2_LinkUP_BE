package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.order.entity.Delivery;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.entity.Payment;
import dev_final_team10.GoodBuyUS.domain.payment.dto.PaymentDTO;
import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import dev_final_team10.GoodBuyUS.domain.payment.dto.RefundDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.DetailProductDTo;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.PaymentRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductPostRepository productPostRepository;
    private final UserRepository userRepository;
    private final PayService payService;
    private final PaymentRepository paymentRepository;

    // 주문을 위해 이전 페이지에서 결정한 수량과 가격을 업데이트
    public DetailProductDTo readyToOrder(CountRequestDTO orderRequestDTO, Long postId){
        /**
         * Todo : 요청 개수가 상품의 현재 재고보다 크진 않은지 , 0은 아닌지
         */
        ProductPost productPost = productPostRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
        if(orderRequestDTO.getAmount() > productPost.getProduct().getStock()){
            throw new IllegalStateException("요청 수량이 재고를 초과했습니다.");
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
        try {
            ProductPost productPost = productPostRepository.findById(postId)
                    .orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NoSuchElementException("없는 회원입니다"));

            if (orderRequestDTO.getQuantity() > productPost.getProduct().getStock()) {
                throw new IllegalArgumentException("주문 수량은 현재 상품의 재고보다 많을 수 없습니다.");
            }

            Order order = Order.createOrder(productPost, user, orderRequestDTO.getQuantity(),
                    orderRequestDTO.getDeliveryRequestDTO().getAddress(),
                    orderRequestDTO.getPrice(), orderRequestDTO.getDeliveryRequestDTO().getNeeded());
            order.registUser(user);
            orderRepository.save(order);

            return new TossRequestDTO(order.getOrderId(), user.getId(), productPost.getPostId(),
                    orderRequestDTO.getPrice(), orderRequestDTO.getQuantity());
        } catch (NoSuchElementException | IllegalArgumentException e) {
            // 적절한 예외 처리: 메시지 로깅 및 전달
            throw new RuntimeException("주문 생성 중 문제가 발생했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 오류가 발생했습니다.", e);
        }
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
            order.defineDelivery(Delivery.COMPLETE);
            order.changeOrderStatus(OrderStatus.COMPLETE);
            product.decreaseStock(tossRequestDTO.getQuantity());
            // 결제에 성공함, 재고가 0이거나 0보다 작을 때 판매 불가 상태로 변환, 재고는 데드라인과 관계 없음
            if(product.getStock() <= 0){
                post.unAvailable();
            }
            log.info("결제 성공 - orderID : {}",order.getOrderId());
            return paymentDTO;
        }
        /**
         * 결제 실패, 실패 url로 리다이렉트, 수량 안 줄어들음
         * 예외처리 커스텀 : 결제 성공을 디폴트, 실패 시 예외처리하면 깔끔해질 것 같음
         */
        else {
            order.changeOrderStatus(OrderStatus.FAILED);
            log.warn("결제 실패 - : orderId : {} 주문 실패",order.getOrderId());
        }
        return paymentDTO;
    }

    /**
     * 주문 내역 조회, 배달 상태도 추가
     */
    public List<OrdersDTO> findUsersOrderList(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NoSuchElementException("잘못된 사용자입니다"));
        List<Order> orders =  user.getOrders();//orderRepository.findOrderByUser(user);
        List<OrdersDTO> ordersDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrdersDTO ordersDTO = new OrdersDTO();
            Payment payment = paymentRepository.findByOrder(order);
            ordersDTO.setProductName(order.getOrderName());
            ordersDTO.setPirce(order.getPrice());
            ordersDTO.setOrderDate(order.getCreatedAt());
            ordersDTO.setPaymentStatus(payment.getPaymentStatus());
            ordersDTO.setPaymentId(payment.getPaymentId());
            ordersDTO.setQuantity(order.getQuantity());
            if (ordersDTO.getPaymentStatus() ==PaymentStatus.REFUND) {
                ordersDTO.setDelivery(Delivery.REFUND);
            } else ordersDTO.setDelivery(Delivery.COMPLETE);
            ordersDTOS.add(ordersDTO);
        }
        return ordersDTOS;
    }

    /**
     * 사용자 환불
     **/
    public String refund(Long payId) {
        try {
            Payment payment = paymentRepository.findById(payId)
                    .orElseThrow(() -> new NoSuchElementException("잘못된 오더"));
            if (payment.getPaymentStatus() == PaymentStatus.REFUND) {
                throw new IllegalStateException("이미 환불된 주문입니다");
            }
            Order order = orderRepository.findById(payment.getOrder().getOrderId())
                    .orElseThrow(() -> new NoSuchElementException("주문 정보를 찾을 수 없습니다"));
            RefundDTO refundDTO = new RefundDTO(order.getOrderId(), payment.getPrice(), LocalDateTime.now());
            return payService.refundPayment(refundDTO);
        } catch (IllegalStateException e) {
            return e.getMessage(); // "이미 환불된 주문입니다"
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
    }

    /**
     * 관리자 환불
     * 데드라인이 지났음에도 불구하고, 할인 가격인 최소 수량에 맞춰 주문이 이루어지지 않은 경우
     * 1. 스케쥴러로 한시간마다 글 검증, 판매불가인 경우 구매 비활성화 전달
     */


}

