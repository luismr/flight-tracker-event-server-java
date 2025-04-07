package dev.luismachadoreis.flighttracker.server.common.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableConfigurationProperties(OpenApiConfig.ApiProperties.class)
public class OpenApiConfig implements WebMvcConfigurer {

    private final ApiProperties properties;

    public OpenApiConfig(ApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui/", "/swagger-ui/index.html");
        registry.addRedirectViewController("/api-docs", "/v3/api-docs");
        registry.addRedirectViewController("/api-docs/", "/v3/api-docs");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = properties.getServers().stream()
            .map(server -> new Server()
                .url(server.getUrl())
                .description(server.getDescription()))
            .toList();

        return new OpenAPI()
            .servers(servers)
            .info(new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact()
                    .name(properties.getContact().getName())
                    .email(properties.getContact().getEmail())
                    .url(properties.getContact().getUrl()))
                .license(new License()
                    .name(properties.getLicense().getName())
                    .url(properties.getLicense().getUrl())));
    }

    @Value
    @ConfigurationProperties(prefix = "api")
    public static class ApiProperties {
        String title;
        String description;
        String version;
        ContactInfo contact;
        LicenseInfo license;
        List<ServerInfo> servers;

        @Value
        public static class ContactInfo {
            String name;
            String email;
            String url;
        }

        @Value
        public static class LicenseInfo {
            String name;
            String url;
        }

        @Value
        public static class ServerInfo {
            String url;
            String description;
        }
    }
} 