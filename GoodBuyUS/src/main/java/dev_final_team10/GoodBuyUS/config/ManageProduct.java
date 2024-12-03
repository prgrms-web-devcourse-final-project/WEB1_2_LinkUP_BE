package dev_final_team10.GoodBuyUS.config;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
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
    private final MainPaymentService mainPaymentService;

    @Scheduled(fixedRate = 600000)
    public void verifyAvailable() {
        List<ProductPost> allProducts = productPostRepository.findAll();
        for (ProductPost productPost : allProducts) {
            /**
             * 현재 시간이 판매 기간보다 더 길거나 같을 때, 주문한 개수가 최소 수량보다 많은지 검증
             * 주문한 개수가 최소 수량보다 많다면 그냥 판매 상태만 불가로 변경
             * 주문한 개수가 최소 수량보다 적다면 구매한 인원들 전부 환불
             */
            if (!productPost.getProduct_period().isAfter(LocalDateTime.now())) {
                log.info("검증 중: 상품 ID {}", productPost.getPostId());

                List<Order> orders = orderRepository.findAllByProductPostAndStatus(productPost, OrderStatus.COMPLETE);

                int totalQuantity = orders.stream().mapToInt(Order::getQuantity).sum();

                if (totalQuantity < productPost.getMinAmount()) {
                    productPost.unAvailable();
                    refundEveryUser(productPost);
                } else {
                    productPost.unAvailable();
                }
            }
        }
    }

    /**
     * 알람 푸시 기능과 연계가 되었으면 좋겠음
     */
    public void refundEveryUser(ProductPost productPost) {
        List<Order> orders = orderRepository.findAllByProductPost(productPost);

        for (Order order : orders) {
            try {
                String paymentKey = order.getOrderId().toString(); // orderId를 결제와 연결된 키로 사용
                mainPaymentService.cancelPayment(
                        paymentKey,
                        "최소 주문량 미달로 환불 처리",
                        order.getPrice() // 환불 금액을 주문 가격으로 설정
                );
                log.info("환불 완료: 주문 ID {}", order.getOrderId());
            } catch (Exception e) {
                log.error("환불 실패: 주문 ID {}, 이유: {}", order.getOrderId(), e.getMessage());
            }
        }
    }
}
