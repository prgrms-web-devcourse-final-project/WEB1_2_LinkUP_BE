package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.entity.PayType;
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

    public CommunityPaymentController(CommunityPaymentService communitypaymentService) {
        this.communityPaymentService = communitypaymentService;
    }

    @PostMapping
    public ResponseEntity<CommunityPaymentResponseDto> createAndRequestPayment(
            @RequestBody CommunityPaymentRequestDto requestDto) {
        System.out.println("createAndRequestPayment 호출됨: " + requestDto.getOrderId());
        System.out.println("요청 데이터: " + requestDto);

        requestDto.setSuccessUrl("http://localhost:8080/api/v1/virtual/success");
        requestDto.setFailUrl("http://localhost:8080/api/v1/virtual/fail");

        CommunityPaymentResponseDto responseDto = communityPaymentService.createAndRequestPayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount) {
        try {
            //paymentService.handlePaymentSuccess(paymentKey, orderId, amount);
            return ResponseEntity.ok("가상계좌 발급이 성공적으로 완료 자동적으로 결제 승인 후 계좌 정보 제공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/fail")
    public ResponseEntity<String> handlePaymentFail(@RequestParam String orderId, @RequestParam String message) {
        try {
            //paymentService.handlePaymentFail(orderId, message);
            return ResponseEntity.badRequest().body("결제가 실패했습니다: " + message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 실패 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
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
                    .body("결제 승인 중 오류 발생: " + e.getMessage());
        }
    }
    @GetMapping("/update-payment/{paymentKey}")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable String paymentKey) {
        try {
            communityPaymentService.updatePaymentStatus(paymentKey);
            return ResponseEntity.ok("결제 상태가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 상태 업데이트 중 오류 발생: " + e.getMessage());
        }
    }

}