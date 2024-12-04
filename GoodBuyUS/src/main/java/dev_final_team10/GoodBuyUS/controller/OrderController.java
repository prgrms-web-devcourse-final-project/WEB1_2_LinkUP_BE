package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.order.dto.*;
import dev_final_team10.GoodBuyUS.domain.product.dto.DetailProductDTo;
import dev_final_team10.GoodBuyUS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goodbuyUs")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/orders")
    public DetailProductDTo readyToOrder(@RequestBody CountRequestDTO countRequestDTO, @RequestParam Long postId) {
        System.out.println("Controller: readyToOrder 호출됨!"); // 간단한 출력
        return orderService.readyToOrder(countRequestDTO, postId);
    }
}
