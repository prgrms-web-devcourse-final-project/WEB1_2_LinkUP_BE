package dev_final_team10.GoodBuyUS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.payment.entity.CommunityPayment;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentRequestDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.CommunityPaymentResponseDto;
import dev_final_team10.GoodBuyUS.domain.payment.dto.TossWebhookDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityPaymentRepository;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CommunityPaymentService {

    private final WebClient webClient;
    private final CommunityPaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final SSEService sseService;
    private final  CommunityPostRepository communityPostRepository;
    private final CommunityService communityService;
    private final ParticipationsRepository participationsRepository;
    private final ChatRoomService chatRoomService;

    public CommunityPaymentService(WebClient.Builder webClientBuilder,
                                   CommunityPaymentRepository paymentRepository,
                                   ObjectMapper objectMapper,
                                   SSEService sseService,
                                   CommunityPostRepository communityPostRepository,
                                   CommunityService communityService,
                                   ParticipationsRepository participationsRepository, ChatRoomService chatRoomService) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth("test_sk_LlDJaYngrooZolGP9mERrezGdRpX", "");
                    headers.set("Content-Type", "application/json");
                })
                .build();
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
        this.sseService = sseService;
        this.communityPostRepository = communityPostRepository;
        this.communityService = communityService;
        this.participationsRepository = participationsRepository;
        this.chatRoomService = chatRoomService;
    }
    @Transactional
    public CommunityPaymentResponseDto createAndRequestPayment(CommunityPaymentRequestDto requestDto) {
        try {
            String rawResponse = webClient.post()
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Toss API 응답 데이터: " + rawResponse);

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);
            System.out.println("Parsed paymentKey: " + responseDto.getPaymentKey());

            CommunityPayment payment = CommunityPayment.builder()
                    .participationsOrderId(requestDto.getOrderId())
                    .amount(requestDto.getAmount())
                    .paymentStatus("WAITING_FOR_APPROVAL")
                    .communityPaymentKey(responseDto.getPaymentKey())
                    .secret(responseDto.getSecret())
                    .communityCreatedAt(LocalDateTime.now())
                    .recipientName(requestDto.getRecipientName())
                    .recipientAddress(requestDto.getRecipientAddress())
                    .deliveryRequest(requestDto.getDeliveryRequest())
                    .build();
            // 배송 정보
            responseDto = responseDto.toBuilder()
                    .recipientName(payment.getRecipientName())
                    .recipientAddress(payment.getRecipientAddress())
                    .deliveryRequest(payment.getDeliveryRequest())
                    .build();
            paymentRepository.saveAndFlush(payment);
            log.info("결제 요청 생성 완료 - Order ID: {}", requestDto.getOrderId());
            return responseDto;
        } catch (Exception e) {
            log.error("결제 요청 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
//가상계좌 결제승인
    @Transactional
    public CommunityPaymentResponseDto confirmPayment(String paymentKey, String orderId, int amount, Long community_post_id, Participations participations) {
        try {
            String rawResponse = webClient.post()
                    .uri("/" + paymentKey)
                    .bodyValue(Map.of(
                            "amount", amount,
                            "orderId", orderId
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("결제 승인 요청에 대한 Toss API 응답: {}", rawResponse);

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);

            CommunityPayment payment = paymentRepository.findByParticipationsOrderId(orderId)
                    .orElseThrow(() -> {
                        log.error("결제 승인 중 데이터 조회 실패. Order ID: {}", orderId);
                        List<CommunityPayment> allPayments = paymentRepository.findAll();
                        log.info("현재 DB에 저장된 Payments: {}", allPayments);
                        return new IllegalArgumentException("해당 주문을 찾을 수 없습니다: " + orderId);
                    });

            payment = payment.toBuilder()
                    .paymentStatus(responseDto.getStatus())
                    .communityPaymentKey(responseDto.getPaymentKey())
                    .accountNumber(responseDto.getVirtualAccount().getAccountNumber())
                    .bankId(responseDto.getVirtualAccount().getBankCode())
                    .customerName(responseDto.getVirtualAccount().getCustomerName())
                    .communityApprovedAt(LocalDateTime.now())
                    .build();
            // 배송 정보
            responseDto = responseDto.toBuilder()
                    .recipientName(payment.getRecipientName()) // 배송 정보 추가
                    .recipientAddress(payment.getRecipientAddress())
                    .deliveryRequest(payment.getDeliveryRequest())
                    .build();

            participations.setStatus(participationStatus.PAYMENT_COMPLETE);
            participationsRepository.save(participations);
            log.info("참여 상태 업데이트: {}", participations);
            CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);

            //참여자 모두가 결제 완료한 경우 글의 상태 PAYMENT_COMPLETE
            if(communityService.getPaymentCount(community_post_id).equals(communityPost.getAvailableNumber())){
                communityPost.setStatus(postStatus.PAYMENT_COMPLETED);
                communityPostRepository.save(communityPost);
                log.info("글 상태가 PAYMENT_COMPLETED로 변경되었습니다. Post ID: {}", community_post_id);

                ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                chatRoomDTO.setPostId(community_post_id);
                chatRoomDTO.setCapacity(participationsRepository.findByCommunityPost(communityPost).size());

                chatRoomService.createChatRoom(chatRoomDTO);
            }
            communityPostRepository.saveAndFlush(communityPost);
            paymentRepository.saveAndFlush(payment); // 동기화 보장
            //communityPostRepository.save(communityPost);
            //paymentRepository.save(payment);
            return responseDto;

        } catch (Exception e) {
            throw new RuntimeException("결제 승인 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
//가상계좌 결제조회
    public CommunityPaymentResponseDto updatePaymentStatus(String paymentKey) {
        try {

            String rawResponse = webClient.get()
                    .uri("/" + paymentKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);


            CommunityPayment payment = paymentRepository.findByCommunityPaymentKey(paymentKey)
                    .orElseThrow(() -> new IllegalArgumentException("해당 결제를 찾을 수 없습니다: " + paymentKey));

            payment = payment.toBuilder()
                    .paymentStatus(responseDto.getStatus()) // Toss에서 받은 status로 업데이트
                    .communityApprovedAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            responseDto = responseDto.toBuilder()
                    .recipientName(payment.getRecipientName()) // 수령인 이름
                    .recipientAddress(payment.getRecipientAddress()) // 주소
                    .deliveryRequest(payment.getDeliveryRequest()) // 배송 요청사항
                    .build();


            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("결제 상태 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }




//가상계좌 결제 취소(상단이랑 다른점은 환불 계좌를 기입해야함)
    @Transactional
    public CommunityPaymentResponseDto cancelPayment(String paymentKey, String cancelReason, Map<String, String> refundAccount) {
        try {

            Map<String, Object> requestBody = Map.of(
                    "cancelReason", cancelReason,
                    "refundReceiveAccount", Map.of(
                            "bank", refundAccount.get("bank"),
                            "accountNumber", refundAccount.get("accountNumber"),
                            "holderName", refundAccount.get("holderName")
                    )
            );
            log.info("Toss API로 전달된 환불 요청 데이터: {}", requestBody);

            String rawResponse = webClient.post()
                    .uri("/{paymentKey}/cancel", paymentKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            CommunityPaymentResponseDto responseDto = objectMapper.readValue(rawResponse, CommunityPaymentResponseDto.class);
            CommunityPayment payment = paymentRepository.findByCommunityPaymentKey(paymentKey)
                    .orElseThrow(() -> new RuntimeException("환불 가능한 금액이 없습니다."));

            if ("CANCELED".equals(responseDto.getStatus())) {
                payment = payment.toBuilder()
                        .paymentStatus("CANCELED")
                        .canceledAt(LocalDateTime.now())
                        .cancelReason(cancelReason)
                        .build();

                paymentRepository.save(payment);
            } else {
                throw new RuntimeException("결제 취소가 실패했습니다: " + responseDto.getStatus());
            }
            return responseDto;

        } catch (Exception e) {
            throw new RuntimeException("결제 취소 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
}


