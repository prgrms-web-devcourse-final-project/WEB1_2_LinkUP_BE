package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class MainPaymentRedirectController {

    private final MainPaymentService mainPaymentService;

    @RequestMapping(value = "/payment-success/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> handleProductPaymentSuccess(
            @PathVariable Long productId,
            @RequestParam String paymentKey,
            @RequestParam UUID orderId,
            @RequestParam int amount) {
        try {
            // 기존 결제 성공 로직 호출
            MainPaymentResponseDto responseDto = mainPaymentService.handlePaymentSuccess(paymentKey, orderId, amount);
            // 응답 반환
            return ResponseEntity.ok(Map.of(
                    "productName", responseDto.getProductName(),
                    "quantity", responseDto.getQuantity(),
                    "price", responseDto.getPrice(),
                    "totalAmount", responseDto.getTotalPrice(),
                    "status", responseDto.getStatus(),
                    "redirectUrl", mainPaymentService.buildRedirectUrl(productId, "success")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 성공 처리 중 오류 발생", "message", e.getMessage()));
        }
    }

    @RequestMapping(value = "/payment-fail/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> handleProductPaymentFail(
            @PathVariable Long productId,
            @RequestParam String orderId,
            @RequestParam String message) {
        try {
            // 필요시 추가(프론트 분들 의견 듣고)
            return ResponseEntity.ok(Map.of(
                    "status", "fail",
                    "message", message,
                    "redirectUrl", mainPaymentService.buildRedirectUrl(productId, "fail")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 실패 처리 중 오류 발생", "message", e.getMessage()));
        }
    }
}
