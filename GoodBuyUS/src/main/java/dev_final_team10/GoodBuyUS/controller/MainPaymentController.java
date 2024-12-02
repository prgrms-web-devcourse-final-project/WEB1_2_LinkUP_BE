package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/main-payments")
@RequiredArgsConstructor
public class MainPaymentController {

    private final MainPaymentService paymentService;

    /**
     * 주문 및 결제 요청 처리
     */
    @PostMapping
    public ResponseEntity<MainPaymentResponseDto> createAndRequestPayment(@RequestBody MainPaymentRequestDto requestDto) {
        MainPaymentResponseDto responseDto = paymentService.createAndRequestPayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 결제 성공 처리
     */
    @PostMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        // 성공 처리 로직
        return ResponseEntity.ok("결제 성공적으로 처리되었습니다.");
    }

    /**
     * 결제 실패 처리
     */
    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFail(@RequestParam String orderId, @RequestParam String message) {
        // 실패 처리 로직
        return ResponseEntity.badRequest().body("결제가 실패했습니다: " + message);
    }
}
