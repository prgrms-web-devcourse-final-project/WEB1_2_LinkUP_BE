package dev_final_team10.GoodBuyUS.controller.api;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrdersDTO;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final CommunityPostRepository communityPostRepository;

    //현재 비밀번호 일치하는지 확인 - 비밀번호 변경 전에
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> request){
        //현재 로그인된 사용자 email 가져오기
        String userEmail = mypageService.getCurrentUserEmail();

        //현재 비밀번호가 맞는지 확인
        if(!mypageService.isCurrentPasswordValid(userEmail, request.get("currentPassword"))){
            return ResponseEntity.badRequest().body(Map.of("error","현재 비밀번호가 일치하지 않습니다."));
        }

        //현재 비밀번호가 맞으면 true 반환
        return ResponseEntity.ok(Map.of("valid", true));
    }


    //비밀번호 변경
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request){
        String userEmail = mypageService.getCurrentUserEmail();

        mypageService.updateNewPassword(userEmail, request.get("newPassword"));
        return ResponseEntity.ok(Map.of("message","비밀번호가 성공적으로 변경되었습니다."));
    }

    //동네 변경
    @PutMapping("/changeneighbor")
    public ResponseEntity<?> changeNeighbor(@RequestBody Map<String, String> request){
        return mypageService.chageNeighbor(request.get("newAddress"));
    }

    //작성한 글이 승인대기 상태일 때 수정가능 하도록
    @PutMapping("/post/{community_post_id}")
    public ResponseEntity<?> modifyPost(@PathVariable("community_post_id") Long communityPostId,
                                        @RequestBody WriteModifyPostDto writeModifyPostDto){
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElse(null);
        //현재 글의 상태가 승인대기가 아니라면 글을 수정하지 못하도록
        if(communityPost.getStatus() != postStatus.NOT_APPROVED){
            return ResponseEntity.badRequest().body(Map.of("error","글을 수정할 수 없는 상태입니다."));
        }
        PostResponseDto postResponseDto = mypageService.modifyPost(writeModifyPostDto, communityPostId);
        return ResponseEntity.ok(Map.of("message", "글이 수정되었습니다.",
                "updatedPost", postResponseDto));
    }

    //내가 작성한 글 목록 보기
    @GetMapping("/post")
    public List<PostResponseDto> myPostList(){
         return mypageService.myPostList();
    }

    //구매내역 보기
    @GetMapping("/orders")
    public ResponseEntity<?> myOrderList(@RequestHeader("Authorization") String token){
        String userEmail = extractEmailFromToken(token);
        // 이메일을 기반으로 주문 목록 조회
        List<OrdersDTO> orders = mypageService.orderlist(userEmail);
        // 결과가 없을 경우
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("주문 내역 없음");
        }
        // 성공 응답
        return ResponseEntity.ok(orders);
    }

    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }

    // 기본 마이페이지에 필요한 정보 전달
    @GetMapping
    public ResponseEntity<?> setting(){
       return mypageService.mypageMain();
    }

//    // 프로필 변경 기능 채원님께 문의
//    @PutMapping("/editprofile")
//    public ResponseEntity<?> editProfile(@RequestParam("profile") MultipartFile multipartFile) throws Exception{
//        return mypageService.editProfile();
//    }
}
