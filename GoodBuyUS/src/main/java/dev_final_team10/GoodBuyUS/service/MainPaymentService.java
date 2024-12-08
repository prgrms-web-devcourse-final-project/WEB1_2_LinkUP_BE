package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrderRequestDTO;
import dev_final_team10.GoodBuyUS.domain.order.entity.Delivery;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.order.entity.OrderStatus;
import dev_final_team10.GoodBuyUS.domain.payment.entity.MainPayment;
import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.domain.product.entity.Product;
import dev_final_team10.GoodBuyUS.domain.product.entity.ProductPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.MainPaymentRepository;
import dev_final_team10.GoodBuyUS.repository.OrderRepository;
import dev_final_team10.GoodBuyUS.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.DProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainPaymentService {

    private final WebClient.Builder webClientBuilder;
    private final MainPaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    public String buildRedirectUrl(Long productId, String status) {
        return String.format(" http://15.164.5.135:8080/products/payment-%s/%d", status, productId);
    }                           // 리다이렉트 주소 변경

    @Transactional
    public MainPaymentResponseDto createAndRequestPayment(Order order) {
        WebClient webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .build();
        // 주문을 만드는 단계
        MainPayment payment = MainPayment.builder()
                .order(order)
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .totalPrice(order.getPrice() * order.getQuantity())
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);


        Long productId = order.getProductPost().getProduct().getProductId();
        String successUrl = buildRedirectUrl(productId, "success");
        String failUrl = buildRedirectUrl(productId, "fail");
        // 토스에 실제적으로 들어가는 것
        try {
            String rawResponse = webClient.post()
                    .bodyValue(Map.of(
                            "orderId", order.getOrderId(),
                            "amount", order.getPrice(),
                            "orderName", order.getOrderName(),
                            "successUrl", successUrl,
                            "failUrl", failUrl,
                            "method", "CARD" // 카드 결제 방식 고정
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Toss API 응답: {}", rawResponse);

            MainPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, MainPaymentResponseDto.class);
            responseDto.setOrderId(order.getOrderId());
            responseDto.setProductName(order.getOrderName());
            responseDto.setQuantity(order.getQuantity());
            responseDto.setPrice(order.getProductPost().getProuctDiscount());
            responseDto.setTotalPrice(responseDto.getPrice()*responseDto.getQuantity());
            responseDto.setStatus(payment.getPaymentStatus().name());
            responseDto.setCreatedAt(payment.getCreatedAt());
            responseDto.setUpdatedAt(payment.getUpdatedAt());

            return responseDto;

        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Transactional
    public MainPaymentResponseDto handlePaymentSuccess(String paymentKey, UUID orderId, int amount) {
        log.info("결제 성공 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("없는 오더입니다."));

        MainPayment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new NoSuchElementException("없는 주문"));

        try {
            WebClient webClient = webClientBuilder.build();
            String response = webClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .bodyValue(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", orderId.toString(),
                            "amount", amount
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("결제 승인 응답: {}", response);

            payment.setPaymentStatus(PaymentStatus.DONE);
            order.defineDelivery(Delivery.COMPLETE);
            order.changeOrderStatus(OrderStatus.COMPLETE);
            payment.setPaymentKey(paymentKey);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            order.getProductPost().getProduct().decreaseStock(order.getQuantity());
            if(order.getProductPost().getProduct().getStock()<=0){
                order.getProductPost().unAvailable();
            }

            log.info("결제 승인 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);

            return MainPaymentResponseDto.builder()
                    .orderId(payment.getOrder().getOrderId())
                    .productName(payment.getOrder().getOrderName())
                    .quantity(payment.getQuantity())
                    .price(payment.getPrice())
                    .totalPrice(payment.getTotalPrice())
                    .status(payment.getPaymentStatus().name())
                    .paymentKey(payment.getPaymentKey())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();

        } catch (Exception e) {
            log.error("결제 승인 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 승인 중 오류 발생: " + e.getMessage(), e);
        }
    }
    //결제 승인
    @Transactional
    public void approvePayment(String paymentKey, String orderId, int amount) {
        log.info("결제 승인 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(()->new NoSuchElementException("없는 오더"));
        MainPayment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (!payment.getPaymentStatus().equals(PaymentStatus.AUTH_COMPLETED)) {
            throw new RuntimeException("이미 승인된 결제이거나 승인할 수 없는 상태입니다.");
        }

        try {
            WebClient webClient = webClientBuilder.build();
            String response = webClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .bodyValue(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", orderId,
                            "amount", amount
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("결제 승인 응답: {}", response);


            payment.setPaymentStatus(PaymentStatus.DONE);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            log.info("결제 승인 처리 완료: Order ID = {}, Payment Key = {}", orderId, paymentKey);

        } catch (Exception e) {
            log.error("결제 승인 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 승인 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
    @Transactional(readOnly = true)
    public Long getProductIdFromOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order를 찾을 수 없습니다: " + orderId));

        // Order -> ProductPost -> Product -> productId 반환
        return order.getProductPost().getProduct().getProductId();
    }

    //결제 취소 및 환불
    @Transactional
    public void cancelPayment(String paymentKey, String cancelReason, Integer cancelAmount) {
        log.info("결제 취소 요청: paymentKey={}, cancelReason={}, cancelAmount={}", paymentKey, cancelReason, cancelAmount);

        MainPayment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        if (!payment.getPaymentStatus().equals(PaymentStatus.DONE) &&
                !payment.getPaymentStatus().equals(PaymentStatus.PARTIAL_CANCELED)) {
            throw new RuntimeException("취소할 수 없는 상태입니다.");
        }

        int effectiveCancelAmount = cancelAmount != null ? cancelAmount : payment.getOrder().getPrice();
        if (payment.getRefundedAmount() + effectiveCancelAmount > payment.getOrder().getPrice()) {
            throw new RuntimeException("취소 금액이 총 결제 금액을 초과할 수 없습니다.");
        }

        try {
            WebClient webClient = webClientBuilder.build();
            Map<String, Object> requestBody = Map.of(
                    "cancelReason", cancelReason,
                    "cancelAmount", effectiveCancelAmount
            );

            String response = webClient.post()
                    .uri("https://api.tosspayments.com/v1/payments/{paymentKey}/cancel", paymentKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("결제 취소 응답: {}", response);

            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            String apiResponseStatus = (String) responseMap.get("status");
            if ("CANCELED".equals(apiResponseStatus)) {
                payment.setPaymentStatus(PaymentStatus.CANCELED);
            } else if ("PARTIAL_CANCELED".equals(apiResponseStatus)) {
                payment.setPaymentStatus(PaymentStatus.PARTIAL_CANCELED);
            }

            Integer updatedBalanceAmount = (Integer) responseMap.get("balanceAmount");
            if (updatedBalanceAmount != null) {
                payment.setBalanceAmount(updatedBalanceAmount);
            }

            payment.setRefundedAmount(payment.getRefundedAmount() + effectiveCancelAmount);
            payment.setCancelReason(cancelReason);
            payment.getOrder().changeOrderStatus(OrderStatus.CANCEL);
            payment.getOrder().defineDelivery(Delivery.REFUND);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            payment.getOrder().getProductPost().getProduct().increaseStock(payment.getQuantity());

            log.info("결제 취소 처리 완료: PaymentKey = {}", paymentKey);

        } catch (Exception e) {
            log.error("결제 취소 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("결제 취소 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public MainPaymentResponseDto getPaymentDetails(String paymentKey) {
        MainPayment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NoSuchElementException("결제 정보를 찾을 수 없습니다."));

        return MainPaymentResponseDto.builder()
                .orderId(payment.getOrder().getOrderId())
                .productName(payment.getOrder().getOrderName())
                .quantity(payment.getQuantity())
                .price(payment.getPrice())
                .totalPrice(payment.getTotalPrice())
                .status(payment.getPaymentStatus().name())
                .paymentKey(payment.getPaymentKey())
                .cancelReason(payment.getCancelReason())
                .refundedAmount(payment.getRefundedAmount())
                .balanceAmount(payment.getBalanceAmount())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

}