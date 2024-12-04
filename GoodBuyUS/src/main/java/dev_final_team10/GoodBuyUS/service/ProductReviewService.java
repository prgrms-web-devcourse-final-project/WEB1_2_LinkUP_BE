package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.repository.ProductPostRepository;
import dev_final_team10.GoodBuyUS.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductPostRepository productPostRepository;
}
