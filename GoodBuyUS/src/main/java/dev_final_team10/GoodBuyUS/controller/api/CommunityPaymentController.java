package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.TossWebhookDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.CommunityPaymentService;
import dev_final_team10.GoodBuyUS.service.UserService;
import dev_final_team10.GoodBuyUS.service.CommunityWebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/v1/virtual")
public class CommunityPaymentController {

    private final CommunityPaymentService communityPaymentService;
    private final CommunityWebhookService communityWebhookService;
    private final UserRepository userRepository;
    private final ParticipationsRepository participationsRepository;
    private final CommunityPostRepository communityPostRepository;
private final CommunityController communityController;

    public CommunityPaymentController(CommunityPaymentService communityPaymentService, CommunityWebhookService communityWebhookService, UserRepository userRepository, ParticipationsRepository participationsRepository, CommunityPostRepository communityPostRepository, CommunityController communityController) {
        this.communityPaymentService = communityPaymentService;
        this.communityWebhookService = communityWebhookService;
        this.userRepository = userRepository;
        this.participationsRepository = participationsRepository;
        this.communityPostRepository = communityPostRepository;
        this.communityController = communityController;
    }

    @PostMapping("/{community_post_id}")
    public ResponseEntity<?> createAndRequestPayment(@RequestBody CommunityPaymentRequestDto requestDto, @PathVariable Long community_post_id) {
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        User user = currentUser();
        Participations participation = participationsInfo(community_post_id, user);
        Random random = new Random();

        try {
            String generatedOrderId = "goodbuyus" + random.nextInt(50000) + 1500;
            log.info("생성된 Order ID: {}", generatedOrderId);
            requestDto.setOrderId("goodbuyus" +  random.nextInt(50000)+1500);   //orderID랜덤으로 생성
            requestDto.setAmount((int) (communityPost.getUnitAmount() * participation.getQuantity()));  //게시물의 개당 가격 * 현재 사용자의 구매 수
            requestDto.setOrderName(community_post_id + "게시물에 대한 사용자" + user.getName() +"사용자의 결제");
            requestDto.setCustomerName(user.getName());
            requestDto.setCustomerEmail(user.getEmail());
            requestDto.setSuccessUrl("https://goodbuyus.store:8080/v1/virtual/success/" + community_post_id + "/" + user.getId());
            requestDto.setFailUrl("https://goodbuyus.store:8080/v1/virtual/fail"+ community_post_id);

            requestDto.setMethod("VIRTUAL_ACCOUNT");
            CommunityPaymentResponseDto responseDto = communityPaymentService.createAndRequestPayment(requestDto);
            log.info("결제 요청 성공: {}", responseDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 요청 중 오류 발생", "message", e.getMessage()));
        }
    }

    @GetMapping("/success/{community_post_id}/{user_id}")
    public ResponseEntity<?> handlePaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount,
            @PathVariable Long community_post_id,
            @PathVariable Long user_id) {
        try {
            log.info("결제 성공 요청 - Payment Key: {}, Order ID: {}, Amount: {}", paymentKey, orderId, amount);
            // 사용자와 참여 정보 가져오기
            User user = userRepository.findById(user_id).orElse(null);
            Participations participations = participationsInfo(community_post_id,user);
            // 결제 승인 처리
            CommunityPaymentResponseDto responseDto = communityPaymentService.confirmPayment(paymentKey, orderId, amount, community_post_id, participations);
            log.info("결제 승인 완료 - Payment Key: {}, Order ID: {}", paymentKey, orderId);
//            communityController.sendStreamingData(community_post_id);
            // 프론트엔드 리다이렉트 URL 생성
            String frontendRedirectUrl = String.format(
                    "https://goodbuyus.store/community/success/%d?status=%s&amount=%d&recipientName=%s&accountNumber=%s&bankId=%s&recipientAddress=%s&deliveryRequest=%s",
                    community_post_id,
                    responseDto.getStatus(),
                    responseDto.getTotalAmount(),
                    URLEncoder.encode(responseDto.getRecipientName(), StandardCharsets.UTF_8),
                    URLEncoder.encode(responseDto.getVirtualAccount().getAccountNumber(), StandardCharsets.UTF_8),
                    URLEncoder.encode(responseDto.getVirtualAccount().getBankCode(), StandardCharsets.UTF_8),
                    URLEncoder.encode(responseDto.getRecipientAddress(), StandardCharsets.UTF_8),
                    URLEncoder.encode(responseDto.getDeliveryRequest(), StandardCharsets.UTF_8)
            );
            /*return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("결제 승인 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 승인 중 오류 발생", "message", e.getMessage()));
        }
    }*/

            // 프론트엔드로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", frontendRedirectUrl)
                    .build();

        } catch (Exception e) {
            log.error("결제 승인 중 오류 발생: {}", e.getMessage(), e);

            // 실패 시 프론트엔드 리다이렉트 URL 생성
            String frontendFailUrl = String.format(
                    "https://goodbuyus.store/community/fail/%d?error=%s",
                    community_post_id,
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8)
            );

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", frontendFailUrl)
                    .build();
        }
    }
/*
    @GetMapping("/fail")
    public ResponseEntity<?> handlePaymentFail(@RequestParam String orderId, @RequestParam String message) {
        return ResponseEntity.badRequest().body(Map.of("status", "fail", "orderId", orderId, "message", message));
    }
*/
    @GetMapping("/fail/{community_post_id}")
    public ResponseEntity<?> handlePaymentFail(
            @RequestParam String orderId,
            @RequestParam String message,
            @PathVariable Long community_post_id) {
        try {
            // 프론트엔드 리다이렉트 URL 생성
            String frontendFailUrl = String.format(
                    "https://goodbuyus.store/community/fail/%d?orderId=%s&error=%s",
                    community_post_id,
                    URLEncoder.encode(orderId, StandardCharsets.UTF_8),
                    URLEncoder.encode(message, StandardCharsets.UTF_8)
            );

            // 프론트엔드로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", frontendFailUrl)
                    .build();
        } catch (Exception e) {
            log.error("결제 실패 처리 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm-payment/{paymentKey}/{community_post_id}")
    public ResponseEntity<?> confirmPayment(
            @PathVariable String paymentKey,
            @RequestBody Map<String, Object> requestBody,
            @PathVariable Long community_post_id) {
        try {
            int amount = (int) requestBody.get("amount");
            String orderId = (String) requestBody.get("orderId");

            User user = currentUser();
            Participations participations = participationsInfo(community_post_id,user);

            CommunityPaymentResponseDto responseDto = communityPaymentService.confirmPayment(paymentKey, orderId, amount, community_post_id, participations);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "결제 승인 중 오류 발생", "message", e.getMessage()));
        }
    }

    @PostMapping("/cancel-payment/{community_post_id}")
    public ResponseEntity<?> cancelPayment(@RequestBody Map<String, Object> requestBody,@PathVariable Long community_post_id) {
        try {
            String paymentKey = (String) requestBody.get("paymentKey");
            String cancelReason = (String) requestBody.get("cancelReason");
            Map<String, String> refundAccount = (Map<String, String>) requestBody.get("refundReceiveAccount");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            Participations participations = participationsInfo(community_post_id,user);

            CommunityPaymentResponseDto responseDto = communityPaymentService.cancelPayment(paymentKey, cancelReason, refundAccount);
            participations.setStatus(participationStatus.PAYMENT_CANCEL);
            participationsRepository.save(participations);

            CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
            if(communityPost.getCloseAt().isAfter(LocalDateTime.now())){
                    communityPost.setStatus(postStatus.APPROVED);
                    communityPost.setPaymentDeadline(null);
                    communityPostRepository.save(communityPost);
//                    communityController.sendStreamingData(community_post_id);
            }   communityPost.setStatus(postStatus.DELETED);
            communityPost.setPaymentDeadline(null);
            communityPostRepository.save(communityPost);


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
    //웹훅 기능 사용시 커뮤니티 결제 서비스에서 웹훅 주석 푸시고 이것도 푸시면 됩니다.
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody TossWebhookDto webhookDto) {
        try {
            communityWebhookService.processWebhook(webhookDto);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //현재 사용자 정보 가져오기
    private User currentUser(){
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    //현재 사용자의 참여 정보 가져오기
    private Participations participationsInfo(Long communityPostId, User user) {

        List<Participations> participations = participationsRepository.findAllByCommunityPost_CommunityPostId(communityPostId);
        log.info("참여자 목록 조회 - Post ID: {}, 참여자 수: {}", communityPostId, participations.size());
        Participations participationInfo = null; // 초기값 설정

        for (Participations participation : participations) {
            if (participation.getUser().equals(user)) {
                participationInfo = participation;
                break;
            }
        }
        if (participationInfo == null) {
            log.warn("참여 정보를 찾을 수 없습니다 - Post ID: {}, User: {}", communityPostId, user.getEmail());
        } else {
            log.info("참여 정보 조회 성공 - Participation: {}", participationInfo);
        }
        return participationInfo;

    }
}
