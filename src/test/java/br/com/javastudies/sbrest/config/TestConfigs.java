package br.com.javastudies.sbrest.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCAL = "http://localhost:8080";
    String ORIGIN_ERUDIO = "https://www.erudio.com.br";   // Funciona
    String ORIGIN_SEMERU = "https://www.semeru.com.br";   // Não funciona
}
