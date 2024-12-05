package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.TossWebhookDto;
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

    @PostMapping("/cancel-payment")
    public ResponseEntity<?> cancelPayment(@RequestBody Map<String, Object> requestBody) {
        try {
            String paymentKey = (String) requestBody.get("paymentKey");
            String cancelReason = (String) requestBody.get("cancelReason");
            Map<String, String> refundAccount = (Map<String, String>) requestBody.get("refundReceiveAccount");

            CommunityPaymentResponseDto responseDto = communityPaymentService.cancelPayment(paymentKey, cancelReason, refundAccount);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 취소 중 오류 발생", "message", e.getMessage()));
        }
    }
/* 아래는 포스트맨 요청 바디값 예시
    {
        "paymentKey": "tviva20241205103158yNcH3",
            "cancelReason": "고객 변심으로 인한 취소",
            "refundReceiveAccount": {
        "bank": "88",
                "accountNumber": "110123456789",
                "holderName": "치토스"
    }
    }
*/


    @GetMapping("/update-payment/{paymentKey}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String paymentKey) {
        try {
            CommunityPaymentResponseDto responseDto = communityPaymentService.updatePaymentStatus(paymentKey);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 상태 조회 중 오류 발생", "message", e.getMessage()));
        }
    }
/* 웹훅 기능 사용시 커뮤니티 결제 서비스에서 웹훅 주석 푸시고 이것도 푸시면 됩니다.
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody TossWebhookDto webhookDto) {
        try {
            communityPaymentService.processWebhook(webhookDto);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }*/
}


