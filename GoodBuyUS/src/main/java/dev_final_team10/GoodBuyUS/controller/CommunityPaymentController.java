package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.CommunityPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/virtual")
public class CommunityPaymentController {

    private final CommunityPaymentService communityPaymentService;

    public CommunityPaymentController(CommunityPaymentService communityPaymentService) {
        this.communityPaymentService = communityPaymentService;
    }

    @PostMapping
    public ResponseEntity<?> createAndRequestPayment(@RequestBody CommunityPaymentRequestDto requestDto) {
        try {
            requestDto.setSuccessUrl("http://localhost:8080/api/v1/virtual/success");
            requestDto.setFailUrl("http://localhost:8080/api/v1/virtual/fail");

            CommunityPaymentResponseDto responseDto = communityPaymentService.createAndRequestPayment(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 요청 중 오류 발생", "message", e.getMessage()));
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) {
        try {
            CommunityPaymentResponseDto responseDto = communityPaymentService.confirmPayment(paymentKey, orderId, amount);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 승인 중 오류 발생", "message", e.getMessage()));
        }
    }

    @GetMapping("/fail")
    public ResponseEntity<?> handlePaymentFail(@RequestParam String orderId, @RequestParam String message) {
        return ResponseEntity.badRequest().body(Map.of("status", "fail", "orderId", orderId, "message", message));
    }

    @PostMapping("/confirm-payment/{paymentKey}")
    public ResponseEntity<?> confirmPayment(
            @PathVariable String paymentKey,
            @RequestBody Map<String, Object> requestBody) {
        try {
            int amount = (int) requestBody.get("amount");
            String orderId = (String) requestBody.get("orderId");

            CommunityPaymentResponseDto responseDto = communityPaymentService.confirmPayment(paymentKey, orderId, amount);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 승인 중 오류 발생", "message", e.getMessage()));
        }
    }

    @GetMapping("/update-payment/{paymentKey}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String paymentKey) {
        try {
            // Toss API 응답값 가져오기
            CommunityPaymentResponseDto responseDto = communityPaymentService.updatePaymentStatus(paymentKey);

            // Toss API 응답값 반환
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 상태 조회 중 오류 발생", "message", e.getMessage()));
        }
    }
}
