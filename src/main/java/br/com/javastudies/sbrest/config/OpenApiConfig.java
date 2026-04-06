package br.com.javastudies.sbrest.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // http://localhost:8080/v3/api-docs
    // http://localhost:8080/swagger-ui/index.html

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST APIs SpringBoot Kubernetes & Docker")
                        .version("v1")
                        .description(" - ")
                        .termsOfService(" - ")
                        .license(new License()
                                .name("Apache 2.0")
                                .url(" -")
                        )
                );
    }
}
