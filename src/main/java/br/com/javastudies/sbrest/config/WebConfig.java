package br.com.javastudies.sbrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configuração de CORS

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                // .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    // Configuração de tipos de arquivos
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // Via EXTENSION. _.xml or _.json Deprecated on Spring Boot 2.6

        // Via QUERY PARAM _.?mediaType=xml
        // Configurar no Controller os tipos de arquivos consumes/produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } no bean @RequestMapping ou nas funções
        // configurer.favorParameter(true)
               // .parameterName("mediaType")
               // .ignoreAcceptHeader(true)
               // .useRegisteredExtensionsOnly(false)
               // .defaultContentType(MediaType.APPLICATION_JSON)
               // .mediaType("json", MediaType.APPLICATION_JSON)
               // .mediaType("xml", MediaType.APPLICATION_XML);

        // Via HEADER PARAM
         configurer.favorParameter(false)
             .ignoreAcceptHeader(false)
             .useRegisteredExtensionsOnly(false)
             .defaultContentType(MediaType.APPLICATION_JSON)
             .mediaType("json", MediaType.APPLICATION_JSON)
             .mediaType("xml", MediaType.APPLICATION_XML)
             .mediaType("yaml", MediaType.APPLICATION_YAML);
    }
}
