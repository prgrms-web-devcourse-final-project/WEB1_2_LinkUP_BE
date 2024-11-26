package dev_final_team10.GoodBuyUS.controller;
import dev_final_team10.GoodBuyUS.domain.order.dto.OrderRequestDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.OrderResponseDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.PostDetailDTO;
import dev_final_team10.GoodBuyUS.domain.product.dto.ProductPostDTO;
import dev_final_team10.GoodBuyUS.service.ProductPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class ProductPostController {
    private final ProductPostService productPostService;

    @GetMapping("/homepage")
    public List<ProductPostDTO> getPosts(){
       return productPostService.findAllProduct();
    }

    @GetMapping("/homepage/{product_id}")
    public PostDetailDTO detailProduct(@PathVariable Long product_id){
        return productPostService.findPost(product_id);
    }

    @PostMapping("/homepage/order/{post_id}")
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long post_id){
        return productPostService.requestPayment(orderRequestDTO, post_id);
    }
}
