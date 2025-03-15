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
