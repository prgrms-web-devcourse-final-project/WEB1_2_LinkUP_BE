package dev_final_team10.GoodBuyUS.config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * 상단 상품를 초기에 미리 생성
 */
@Component
public class initDB {
    @PostConstruct
    public void createProduct(){
    }
}
