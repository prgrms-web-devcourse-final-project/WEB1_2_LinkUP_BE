package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.PayType;
import dev_final_team10.GoodBuyUS.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.CommunityPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/virtual")
public class CommunityPaymentController {

    private final CommunityPaymentService paymentService;

    public CommunityPaymentController(CommunityPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<CommunityPaymentResponseDto> createAndRequestPayment(@RequestBody CommunityPaymentRequestDto requestDto) {
        System.out.println("createAndRequestPayment 호출됨: " + requestDto.getOrderId());
        System.out.println("요청 데이터: " + requestDto);

        requestDto.setSuccessUrl("http://localhost:8080/api/v1/virtual/success");
        requestDto.setFailUrl("http://localhost:8080/api/v1/virtual/fail");

        // PayType 추가로 로직 확장
        if (requestDto.getPayType() == PayType.VIRTUAL_ACCOUNT) { // payType 때문에 추가
            System.out.println("결제 방식: 가상계좌");
        } else if (requestDto.getPayType() == PayType.CARD) {
            System.out.println("결제 방식: 카드");
        }

        CommunityPaymentResponseDto responseDto = paymentService.createAndRequestPayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount) {
        try {
            //paymentService.handlePaymentSuccess(paymentKey, orderId, amount);
            return ResponseEntity.ok("결제가 성공적으로 완료 y.");
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
    public ResponseEntity<?> confirmPaymentAndSave(
            @PathVariable String paymentKey,
            @RequestBody Map<String, Object> requestBody) {
        try {
            int amount = (int) requestBody.get("amount");
            String orderId = (String) requestBody.get("orderId");

            // 서비스 호출
            CommunityPaymentResponseDto responseDto = paymentService.confirmAndSavePayment(paymentKey, orderId, amount);

            // 전체 응답값 반환
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 승인 중 오류 발생: " + e.getMessage());
        }
    }
    @GetMapping("/update-payment/{paymentKey}")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable String paymentKey) {
        try {
            paymentService.updatePaymentStatus(paymentKey); // 서비스 호출
            return ResponseEntity.ok("결제 상태가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 상태 업데이트 중 오류 발생: " + e.getMessage());
        }
    }



}