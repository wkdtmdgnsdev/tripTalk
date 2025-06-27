package org.kosa.tripTalk;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

//백엔드에서 프론트엔드 서버 개방용 (글로벌 적용)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // REST API 경로 전체 허용
                .allowedOrigins("http://localhost:5173") // 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 세션 등 쿠키도 포함 시 필요
    }
}
