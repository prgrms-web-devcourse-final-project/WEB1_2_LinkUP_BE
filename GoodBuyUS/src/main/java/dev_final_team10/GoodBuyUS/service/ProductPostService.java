package dev_final_team10.GoodBuyUS.service;
import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Todo
 * 1. 재고가 0일 때 판매 중지 : 구매하는 만큼 수량을 줄이고 수량이 0일 버튼 비활성화 시켜야함 > 어떻게하지?
 * 2. 마감기한이 다 됐을 때 판매 중지 : 어떻게 하지 ?
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProductPostService {
    private final ProductPostRepository productPostRepository;

}
