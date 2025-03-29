package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.payment.dto.MainPaymentResponseDto;
import dev_final_team10.GoodBuyUS.service.MainPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

            // 프론트엔드 URL 생성
            String frontendSuccessUrl = String.format(
                    "http://goodbuyus.store/products/payment-success/%d?productName=%s&quantity=%d&price=%d&totalAmount=%d&status=%s",
                    productId,
                    URLEncoder.encode(responseDto.getProductName(), StandardCharsets.UTF_8), // 한글 인코딩
                    responseDto.getQuantity(),
                    responseDto.getPrice(),
                    responseDto.getTotalPrice(),
                    responseDto.getStatus()
            );
            return ResponseEntity.status(HttpStatus.FOUND) // HTTP 302 리다이렉트
                    .header("Location", frontendSuccessUrl)
                    .build();
        } catch (Exception e) {
            String frontendFailUrl = String.format(
                    "http://goodbuyus.store/products/payment-fail/%d?error=%s",
                    productId,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.FOUND) // HTTP 302 리다이렉트
                    .header("Location", frontendFailUrl)
                    .build();
        }
    }

    @RequestMapping(value = "/payment-fail/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> handleProductPaymentFail(
            @PathVariable Long productId,
            @RequestParam(required = false) String paymentKey,
            @RequestParam(required = false) UUID orderId,
            @RequestParam(required = false) String message) {
        try {
            MainPaymentResponseDto responseDto = null;
            if (paymentKey != null && orderId != null) {
                responseDto = mainPaymentService.getPaymentDetails(paymentKey);
            }

            String frontendFailUrl = String.format(
                    "http://goodbuyus.store/products/payment-fail/%d?productName=%s&quantity=%d&price=%d&totalAmount=%d&status=%s&error=%s",
                    productId,
                    responseDto != null ? URLEncoder.encode(responseDto.getProductName(), StandardCharsets.UTF_8) : "Unknown",
                    responseDto != null ? responseDto.getQuantity() : 0,
                    responseDto != null ? responseDto.getPrice() : 0,
                    responseDto != null ? responseDto.getTotalPrice() : 0,
                    responseDto != null ? responseDto.getStatus() : "FAIL",
                    URLEncoder.encode(message != null ? message : "Unknown error", StandardCharsets.UTF_8)
            );
            return ResponseEntity.status(HttpStatus.FOUND) // HTTP 302 리다이렉트
                    .header("Location", frontendFailUrl)
                    .build();
        } catch (Exception e) {
            String fallbackFailUrl = String.format(
                    "http://goodbuyus.store/products/payment-fail/%d?error=%s",
                    productId,
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8)
            );
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", fallbackFailUrl)
                    .build();
        }
    }
}

