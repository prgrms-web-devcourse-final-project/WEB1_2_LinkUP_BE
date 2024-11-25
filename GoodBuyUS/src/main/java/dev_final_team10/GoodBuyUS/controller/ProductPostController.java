package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.category.DetailCategory;
import dev_final_team10.GoodBuyUS.dto.productpost.PostDetailDTO;
import dev_final_team10.GoodBuyUS.dto.productpost.ProductPostDTO;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class ProductPostController {
    private final ProductPostService productPostService;

    @GetMapping("/homepage")
    public List<ProductPostDTO> getPosts(){
       return productPostService.findAllProduct();
    }

    @GetMapping("/homepage/{product_name}")
    public PostDetailDTO detailProduct(@RequestParam String product_name){

    }

}
