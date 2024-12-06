package dev_final_team10.GoodBuyUS.controller.api;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrderRequestDTO;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
import dev_final_team10.GoodBuyUS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/main-payments")
@RequiredArgsConstructor
public class MainPaymentController {

    private final MainPaymentService mainPaymentService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<MainPaymentResponseDto> createAndRequestPayment(
            @RequestBody OrderRequestDTO orderRequestDTO,
            @RequestHeader("Authorization") String token,
            @RequestParam Long postId) {

        String userEmail = extractEmailFromToken(token);
        Order order = orderService.createOrder(orderRequestDTO, userEmail, postId);
        MainPaymentResponseDto responseDto = mainPaymentService.createAndRequestPayment(order);
        return ResponseEntity.ok(responseDto);
    }

    /*@RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam UUID orderId,
            @RequestParam int amount) {
        try {
            MainPaymentResponseDto responseDto = mainPaymentService.handlePaymentSuccess(paymentKey, orderId, amount);
            Long productId = mainPaymentService.getProductIdFromOrder(orderId);

            String redirectUrl = "http://15.164.5.135:8080/products/payment-success/" + productId;

            return ResponseEntity.ok(Map.of(
                    "productName", responseDto.getProductName(),
                    "quantity", responseDto.getQuantity(),
                    "price", responseDto.getPrice(),
                    "totalAmount", responseDto.getTotalPrice(),
                    "status", responseDto.getStatus(),
                    "redirectUrl", redirectUrl

            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 성공 처리 중 오류 발생", "message", e.getMessage()));
        }
    }*/

    /*@PostMapping("/fail")
    public ResponseEntity<?> handlePaymentFail(
            @RequestParam String orderId,
            @RequestParam String message) {
        try {
            // Order ID를 기반으로 Product ID 가져오기
            Long productId = mainPaymentService.getProductIdFromOrder(UUID.fromString(orderId));


            String redirectUrl = "http://15.164.5.135:8080/products/payment-fail/" + productId;

            return ResponseEntity.ok(Map.of(
                    "status", "fail",
                    "redirectUrl", redirectUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 실패 처리 중 오류 발생", "message", e.getMessage()));
        }
    }*/

    @PostMapping("/approve")
    public ResponseEntity<MainPaymentResponseDto> approvePayment(@RequestBody Map<String, Object> requestBody) {
        String paymentKey = (String) requestBody.get("paymentKey");
        String orderId = (String) requestBody.get("orderId");
        int amount = (int) requestBody.get("amount");

        mainPaymentService.approvePayment(paymentKey, orderId, amount);
        MainPaymentResponseDto responseDto = mainPaymentService.getPaymentDetails(paymentKey);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/cancel")
    public ResponseEntity<MainPaymentResponseDto> cancelPayment(@RequestBody Map<String, Object> requestBody) {
        String paymentKey = (String) requestBody.get("paymentKey");
        String cancelReason = (String) requestBody.get("cancelReason");
        Integer cancelAmount = (Integer) requestBody.get("cancelAmount");

        mainPaymentService.cancelPayment(paymentKey, cancelReason, cancelAmount);
        MainPaymentResponseDto responseDto = mainPaymentService.getPaymentDetails(paymentKey);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{paymentKey}")
    public ResponseEntity<MainPaymentResponseDto> getPaymentDetails(@PathVariable String paymentKey) {
        MainPaymentResponseDto responseDto = mainPaymentService.getPaymentDetails(paymentKey);
        return ResponseEntity.ok(responseDto);
    }

    private String extractEmailFromToken(String token) {
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }
}
