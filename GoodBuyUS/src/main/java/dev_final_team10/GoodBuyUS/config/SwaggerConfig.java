package dev_final_team10.GoodBuyUS.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        io.swagger.v3.oas.models.info.Info info = new Info()
                .version("v1.0.0")
                .title("API - GoodBuyUs")
                .description("API 테스트");

        return new OpenAPI()
                .info(info);  // info 객체를 OpenAPI에 설정
    }
}
