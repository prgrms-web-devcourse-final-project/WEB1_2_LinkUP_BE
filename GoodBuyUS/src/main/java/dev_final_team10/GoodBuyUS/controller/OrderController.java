package dev_final_team10.GoodBuyUS.controller;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.payment.dto.PaymentDTO;
import dev_final_team10.GoodBuyUS.domain.payment.entity.PaymentStatus;
import dev_final_team10.GoodBuyUS.domain.product.dto.DetailProductDTo;
import dev_final_team10.GoodBuyUS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goodbuyUs")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/orders/{postId}")
    public DetailProductDTo readyToOrder(@RequestBody CountRequestDTO countRequestDTO, @PathVariable Long postId) {
        return orderService.readyToOrder(countRequestDTO, postId);
    }

    // 성공 실패 URl 추가 예정
    @PostMapping("/orders/{postId}/payment")
    public PaymentDTO payment(@RequestBody OrderRequestDTO orderRequestDTO,
                              @RequestHeader("Authorization") String token,
                              @PathVariable Long postId){
        String userEmail = extractEmailFromToken(token);
        TossRequestDTO tossRequestDTO = orderService.createOrder(orderRequestDTO, userEmail, postId);
        PaymentDTO paymentDTO =  orderService.payment(tossRequestDTO);
        if(paymentDTO.getStatus() == PaymentStatus.FAIL){
            paymentDTO.setRedirectURL("실패 url");
        }
        else paymentDTO.setRedirectURL("성공 url");
        return paymentDTO;
    }

    @GetMapping("/user")
    public List<OrdersDTO> findOrder(@RequestHeader("Authorization") String token){
        String email = extractEmailFromToken(token);
        return orderService.findUsersOrderList(email);
    }

    // 내 주문 내역에서 환불을 선택했다고 가정
    @PutMapping("/orders/refunds/{payId}")
    public String refundOrder(@PathVariable Long payId){
        return orderService.refund(payId);
    }

    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }


}
