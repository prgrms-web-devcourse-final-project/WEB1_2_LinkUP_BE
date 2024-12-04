package dev_final_team10.GoodBuyUS.controller;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrderRequestDTO;
import dev_final_team10.GoodBuyUS.domain.order.entity.Order;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentRequestDto;
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
        Order order = orderService.createOrder(orderRequestDTO,userEmail,postId);
        MainPaymentResponseDto responseDto = mainPaymentService.createAndRequestPayment(order);
        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<MainPaymentResponseDto> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam UUID orderId,
            @RequestParam int amount) {

        MainPaymentResponseDto responseDto = mainPaymentService.handlePaymentSuccess(paymentKey, orderId, amount);
        return ResponseEntity.ok(responseDto);
    }



    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFail(
            @RequestParam String orderId,
            @RequestParam String message) {
        return ResponseEntity.badRequest().body("결제가 실패했습니다: " + message);
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
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }

}
