package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //자체 회원가입 기능
    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        return userService.signUp(userSignUpDto);

    }
    //JWT 잘 동작하는지 확인하기 위한 메소드 - 나중에 삭제
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

}
