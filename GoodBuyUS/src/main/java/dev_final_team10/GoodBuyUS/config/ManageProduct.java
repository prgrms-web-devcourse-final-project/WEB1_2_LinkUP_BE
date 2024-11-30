package dev_final_team10.GoodBuyUS.config;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.dto.RefundDTO;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManageProduct {
    private final ProductPostRepository productPostRepository;
    private final OrderRepository orderRepository;
    private final PayService payService;
    @Scheduled(fixedRate = 1800000)  // 1800000ms = 30분
    public void verifyAvailable(){
        List<ProductPost> allproducts = productPostRepository.findAll();
        for (ProductPost productPost : allproducts) {
            /**
             * 현재 시간이 판매 기간보다 더 길거나 같을 때, 주문한 개수가 최소 수량보다 많은지 검증
             * 주문한 개수가 최소 수량보다 많다면 그냥 판매 상태만 불가로 변경
             * 주문한 개수가 최소 수량보다 적다면 구매한 인원들 전부 환불
             */
            if(!productPost.getProduct_period().isAfter(LocalDateTime.now())){
                List<Order> orders = orderRepository.findAllByProductPostAndStatus(productPost, OrderStatus.COMPLETE);
                //주문한 수량 계산하기
                int amount = 0;
                for (Order order : orders) {
                    amount += order.getQuantity();
                }
                if(amount < productPost.getMinAmount()){
                    productPost.unAvailable();
                    refundEveryUser(productPost); // 그 상품을 주문한 모든 사람들을 환불처리
                }
                else productPost.unAvailable();
            }
        }
    }

    /**
     * 알람 푸시 기능과 연계가 되었으면 좋겠음
     */
    public void refundEveryUser(ProductPost productPost){
        // 주문이 정상적으로 완료 되었고, 최소 공구 수량을 못 채운 상품을 주문한 주문들
        List<Order> orders = orderRepository.findAllByProductPost(productPost);
        for (Order order : orders) {
            try {
                RefundDTO refundDTO = new RefundDTO(order.getOrderId(), order.getPrice(), LocalDateTime.now());
                payService.refundPayment(refundDTO); // 환불 처리
                log.info("주문 환불 처리 완료: orderId = {}", order.getOrderId());
            } catch (Exception e) {
                log.error("주문 환불 실패: orderId = {}, 이유: {}", order.getOrderId(), e.getMessage());
            }
        }
    }
}

