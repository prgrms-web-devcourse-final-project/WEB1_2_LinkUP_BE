package dev_final_team10.GoodBuyUS.controller.all;
import com.auth0.jwt.JWT;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ReviewRequestDTO;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goodbuyUs")
@RequiredArgsConstructor
@Tag(name = "상단 상품 판매", description = "전체 상품 조회, 상세 상품 조회 api")
public class ProductPostController {
    private final ProductPostService productPostService;

    @Operation(summary = "전체 판매 상품 조회")
    @GetMapping()
    public List<ProductPostDTO> getPosts(){
       return productPostService.findAllProduct();
    }

    @Operation(summary = "상세 상품 조회")
    @GetMapping("/product")
    public PostDetailDTO detailProduct(@RequestParam Long postid){
        return productPostService.findPost(postid);
    }
}
