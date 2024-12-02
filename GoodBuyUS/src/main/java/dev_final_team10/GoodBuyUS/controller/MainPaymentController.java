package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/main-payments")
@RequiredArgsConstructor
public class MainPaymentController {

    private final MainPaymentService paymentService;

    @PostMapping
    public ResponseEntity<MainPaymentResponseDto> createAndRequestPayment(@RequestBody MainPaymentRequestDto requestDto) {
        MainPaymentResponseDto responseDto = paymentService.createAndRequestPayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) {
        paymentService.handlePaymentSuccess(paymentKey, orderId, amount);
        return ResponseEntity.ok("결제 요청이 성공적으로 완료되었습니다.");
    }


    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFail(
            @RequestParam String orderId,
            @RequestParam String message) {
        return ResponseEntity.badRequest().body("결제가 실패했습니다: " + message);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approvePayment(@RequestBody Map<String, Object> requestBody) {
        String paymentKey = (String) requestBody.get("paymentKey");
        String orderId = (String) requestBody.get("orderId");
        int amount = (int) requestBody.get("amount");

        paymentService.approvePayment(paymentKey, orderId, amount);
        return ResponseEntity.ok("결제가 성공적으로 승인되었습니다.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(@RequestBody Map<String, Object> requestBody) {
        String paymentKey = (String) requestBody.get("paymentKey");
        String cancelReason = (String) requestBody.get("cancelReason");
        Integer cancelAmount = (Integer) requestBody.get("cancelAmount"); // null 가능

        try {
            paymentService.cancelPayment(paymentKey, cancelReason, cancelAmount);
            return ResponseEntity.ok("결제가 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }



}
