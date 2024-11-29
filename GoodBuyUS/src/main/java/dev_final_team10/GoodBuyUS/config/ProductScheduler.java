package dev_final_team10.GoodBuyUS.config;

import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.PaymentRepository;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductScheduler {
    private final ProductPostRepository productPostRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PaymentRepository paymentRepository;

    /**
     * 데드라인이 지나 판매가 불가능한 놈들 중 주문 개수가 최소 개수보다 작은 경우 환불 진행
     */
    @Scheduled(cron = "0 0 * * * *") // 매 시간 정각에 실행
    public void verifyProductsAndProcessRefunds() {
        /**
         * 판매가 불가능한 두가지 이유
         * 1. 재고가 다 나가서 판매 불가
         * 2. 데드라인이 됐는데 재고가 안 나가서 환불처리 및 판매 불가
         */
        List<ProductPost> unavailableProducts = productPostRepository.findAllByAvailableIsFalse();
        for (ProductPost productPost : unavailableProducts) {
            List<Order> orders = orderRepository.findAllByProductPost(productPost);

        }
    }
}
