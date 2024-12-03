package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.order.entity.Delivery;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.product.dto.DetailProductDTo;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.MainPaymentRepository;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MainPaymentService mainPaymentService;
    private final MainPaymentRepository mainPaymentRepository;

    public DetailProductDTo readyToOrder(CountRequestDTO orderRequestDTO, Long postId) {
        ProductPost productPost = productPostRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("잘못된 게시글입니다 - 주문"));
        if (orderRequestDTO.getAmount() > productPost.getProduct().getStock()) {
            throw new IllegalStateException("요청 수량이 재고를 초과했습니다.");
        }
        return DetailProductDTo.createDTO(productPost, orderRequestDTO.getAmount());
    }

    public Order createOrder(OrderRequestDTO orderRequestDTO, String userEmail, Long postId) {
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
        return order;
    }

//    public String refund(String paymentKey, String cancelReason, Integer refundAmount) {
//        try {
//            mainPaymentService.cancelPayment(paymentKey, cancelReason, refundAmount);
//            return "환불 처리가 완료되었습니다.";
//        } catch (Exception e) {
//            return "환불 처리 중 오류가 발생했습니다: " + e.getMessage();
//        }
//    }
}
