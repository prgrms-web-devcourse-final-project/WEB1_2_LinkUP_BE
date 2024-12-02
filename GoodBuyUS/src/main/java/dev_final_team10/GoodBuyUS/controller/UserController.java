package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpEmailDto;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    //자체 회원가입 기능 - 이름, 이메일, 비밀번호, 전화번호
    @PostMapping("/checkemail")
    public ResponseEntity<?> signUpCheckEmail(@RequestBody UserSignUpEmailDto userSignUpEmailDto)
            throws Exception {
        return userService.signUpCheckEmail(userSignUpEmailDto);
    }

    //자체 회원가입 기능 - 주소
    @PostMapping("/checkaddress")
    public ResponseEntity<?> signUpCheckAddress(Map<String, String> request) {
        return new ResponseEntity<>(Map.of("message", "주소 확인 완료"), HttpStatus.OK);
    }

    //자체 회원가입 기능 - 닉네임
    @PostMapping("/checknickname")
    public ResponseEntity<?> signUpCheckNickname(@RequestBody Map<String, String> request) {
        return userService.signUpCheckNickname(request.get("nickname"));
    }

    //자체 회원가입 기능 - DB저장
    @PostMapping
    public ResponseEntity<?> signUp(@RequestPart("user") UserSignUpDto userSignUpDto,  // 나머지 데이터는 DTO(JSON)로 받기
                                    @RequestPart("profile") MultipartFile profile) throws Exception {  // 프로필 이미지는 파일로 받기

        return userService.signUp(userSignUpDto, profile);
    }



    //비밀번호 찾기 기능
    @PostMapping("/find")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> request) throws MessagingException, IOException {
        return userService.findPassword(request.get("email"));
    }

    //비밀번호 재설정 가능한지 확인 - 이메일 링크 눌렀을 때 (토큰 유효성 확인)
    @GetMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token){
        if(!jwtService.isTokenValid(token)){
            return ResponseEntity.badRequest().body(Map.of("error","유효하지 않거나 만료된 토큰입니다."));
        }
        return ResponseEntity.ok(Map.of("message","비밀번호 재설정이 가능합니다."));
    }

    //비밀번호 찾기 후 최종 재설정 - 링크가 올바른지 확인 됐을 때
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request){
        if(!jwtService.isTokenValid(token)){
            return ResponseEntity.badRequest().body(Map.of("error","유효하지 않거나 만료된 토큰입니다."));
        }
        return userService.updatePassword(token, request.get("newPassword"));
    }

    

}
