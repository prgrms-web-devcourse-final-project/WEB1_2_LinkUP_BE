package dev_final_team10.GoodBuyUS.controller.api;

import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.product.dto.WishDetails;
import dev_final_team10.GoodBuyUS.domain.product.dto.WishListDto;
import dev_final_team10.GoodBuyUS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/wish")
@RequiredArgsConstructor
public class WishListController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addWishList(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody WishListDto wishListDto){
        if(token == null){
            return ResponseEntity.ok("찜 기능은 로그인 이후 사용할 수 있습니다.");
        }
        String email = extractEmailFromToken(token);
        return userService.addWishList(email, wishListDto);
    }

    @GetMapping
    public ResponseEntity<List<WishDetails>> getWishDetails(@RequestHeader("Authorization")String token){
        String email = extractEmailFromToken(token);
        return userService.getWishDetails(email);
    }


    private String extractEmailFromToken(String token) {
        // 토큰에서 userId를 디코딩하는 로직
        String tokenValue = token.replace("Bearer ", "");
        return JWT.decode(tokenValue).getClaim("email").asString();
    }
}
