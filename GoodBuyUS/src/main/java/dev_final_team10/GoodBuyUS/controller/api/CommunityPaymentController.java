package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/virtual")
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
            requestDto.setOrderId("goodbuyus" +  random.nextInt(50000)+1500);   //orderID랜덤으로 생성
            requestDto.setAmount((int) (communityPost.getUnitAmount() * participation.getQuantity()));  //게시물의 개당 가격 * 현재 사용자의 구매 수
            requestDto.setOrderName(community_post_id + "게시물에 대한 사용자" + user.getName() +"사용자의 결제");
            requestDto.setCustomerName(user.getName());
            requestDto.setCustomerEmail(user.getEmail());
            requestDto.setSuccessUrl("http://15.164.5.135:8080/api/v1/virtual/success/" + community_post_id + "/" + user.getId());
            requestDto.setFailUrl("http://15.164.5.135:8080/api/v1/virtual/fail"+ community_post_id);
            requestDto.setMethod("VIRTUAL_ACCOUNT");
            CommunityPaymentResponseDto responseDto = communityPaymentService.createAndRequestPayment(requestDto);
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
            User user = userRepository.findById(user_id).orElse(null);
            Participations participations = participationsInfo(community_post_id,user);



            CommunityPaymentResponseDto responseDto = communityPaymentService.confirmPayment(paymentKey, orderId, amount, community_post_id, participations);
            communityController.sendStreamingData(community_post_id);
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
        Participations participationInfo = null; // 초기값 설정

        for (Participations participation : participations) {
            if (participation.getUser().equals(user)) {
                participationInfo = participation;
                break;
            }
        }
        return participationInfo;

    }
}


