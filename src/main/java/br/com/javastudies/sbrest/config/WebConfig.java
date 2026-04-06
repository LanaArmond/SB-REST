package br.com.javastudies.sbrest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
