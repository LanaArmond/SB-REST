package br.com.javastudies.sbrest.integrationtests.controller.withjson;

import br.com.javastudies.sbrest.config.TestConfigs;
import br.com.javastudies.sbrest.integrationtests.dto.AccountCredentialsDTO;
import br.com.javastudies.sbrest.integrationtests.dto.TokenDTO;
import br.com.javastudies.sbrest.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenDTO token;

    @BeforeAll
    static void setUp() {
        token = new TokenDTO();
    }

    @Test
    @Order(1)
    void signIn() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        token = given()
                .basePath("/auth/sign")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                    .when()
                .post()
                    .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenDTO.class);

        Assertions.assertNotNull(token.getAccessToken());
        Assertions.assertNotNull(token.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() {

        token = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("username", token.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
                .when()
                    .put("{username}")
                        .then()
                            .statusCode(200)
                                .extract()
                                .body()
                                .as(TokenDTO.class);
    }
}