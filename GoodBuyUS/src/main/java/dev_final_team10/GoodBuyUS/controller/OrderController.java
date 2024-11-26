package dev_final_team10.GoodBuyUS.controller;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.order.dto.PaymentRequestDTO;
import dev_final_team10.GoodBuyUS.domain.order.dto.PaymentResponseDTO;
import dev_final_team10.GoodBuyUS.domain.order.dto.TossRequestDTO;
import dev_final_team10.GoodBuyUS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/payments")
    public PaymentResponseDTO<?> createOrder(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                      @RequestHeader("Authorization") String token){
        String userEmail = extractEmailFromToken(token);
        UUID orderId = orderService.requestPayment(paymentRequestDTO, userEmail);
        TossRequestDTO tossRequestDTO = orderService.matchTossRequest(orderId);
        return orderService.payment(tossRequestDTO);
    }

    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }
}
