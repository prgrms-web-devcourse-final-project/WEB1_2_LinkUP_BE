
package dev_final_team10.GoodBuyUS.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${toss.secret.key}") // application.properties에서 키를 읽어옵니다.
    private String tossSecretKey;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeaders(headers -> headers.setBasicAuth(tossSecretKey, "")); // 시크릿 키 설정
    }
}

