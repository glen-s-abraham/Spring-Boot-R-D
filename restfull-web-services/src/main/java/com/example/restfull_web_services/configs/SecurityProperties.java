package com.example.restfull_web_services.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Typed binding for custom security properties defined in
 * application.properties.
 *
 * Properties prefix: app.security
 * app.security.admin-user = admin username (default: "admin")
 * app.security.admin-password = admin password (default: "admin")
 *
 * Override these in production via environment variables:
 * APP_SECURITY_ADMIN_USER=...
 * APP_SECURITY_ADMIN_PASSWORD=...
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private String adminUser = "admin";
    private String adminPassword = "admin";
}
