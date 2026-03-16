package com.example.restfull_web_services.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Manually registers the HAL Explorer static assets.
 *
 * Normally Spring Data REST auto-config registers this resource handler, but
 * since we excluded SpringDocDataRestConfiguration (to avoid a missing bean
 * error), we wire it up ourselves here.
 *
 * The HAL Explorer jar packages its UI at:
 * classpath:/META-INF/spring-data-rest/hal-explorer/
 *
 * After this config is in place, browse to:
 * http://localhost:8080/explorer/index.html
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/explorer/**")
                .addResourceLocations("classpath:/META-INF/spring-data-rest/hal-explorer/");
    }
}
