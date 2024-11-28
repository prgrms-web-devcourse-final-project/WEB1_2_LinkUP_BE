package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

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

}
