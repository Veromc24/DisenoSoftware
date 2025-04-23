package edu.uclm.esi.circuits.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all paths in the application
                .allowedOrigins("http://localhost:4200") // Allow requests from the Angular frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Explicitly allow OPTIONS
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(false); // Set to true if you need cookies/auth headers, otherwise false is safer
    }
}