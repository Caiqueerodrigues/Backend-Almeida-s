package Development.Rodrigues.Almeidas_Cortes.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000", 
                "http://localhost:3003", 
                "https://almeidascorte.netlify.app", 
                "http://192.168.0.13:3000", 
                "https://almeidas-vps.netlify.app",
                "http://frontend:3000",
                "http://app:3003"
                )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
