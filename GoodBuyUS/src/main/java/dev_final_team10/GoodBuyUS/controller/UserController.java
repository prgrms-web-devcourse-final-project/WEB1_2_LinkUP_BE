package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //자체 회원가입 기능
    @PostMapping
    public ResponseEntity<String> signUp( @RequestPart("user") UserSignUpDto userSignUpDto,  // 나머지 데이터는 DTO로 받기
                                          @RequestPart(value = "profile", required = false) MultipartFile profile) throws Exception {  // 프로필 이미지는 파일로 받기

        return userService.signUp(userSignUpDto, profile);

    }
    //JWT 잘 동작하는지 확인하기 위한 메소드 - 나중에 삭제
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

}
