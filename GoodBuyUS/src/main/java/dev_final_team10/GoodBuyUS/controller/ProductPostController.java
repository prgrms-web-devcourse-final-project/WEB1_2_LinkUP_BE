package dev_final_team10.GoodBuyUS.controller;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goodbuyUs")
@RequiredArgsConstructor
public class ProductPostController {
    private final ProductPostService productPostService;

    @GetMapping()
    public List<ProductPostDTO> getPosts(){
       return productPostService.findAllProduct();
    }

    @GetMapping("/product")
    public PostDetailDTO detailProduct(@RequestParam Long postid){
        return productPostService.findPost(postid);
    }
}
