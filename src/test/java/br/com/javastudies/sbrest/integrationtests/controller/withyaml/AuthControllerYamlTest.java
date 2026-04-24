package br.com.javastudies.sbrest.integrationtests.controller.withyaml;

import br.com.javastudies.sbrest.config.TestConfigs;
import br.com.javastudies.sbrest.integrationtests.controller.withyaml.Mapper.YAMLMapper;
import br.com.javastudies.sbrest.integrationtests.dto.AccountCredentialsDTO;
import br.com.javastudies.sbrest.integrationtests.dto.TokenDTO;
import br.com.javastudies.sbrest.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static TokenDTO token;
    private static YAMLMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();

        token = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials =
                new AccountCredentialsDTO("leandro", "admin123");

        var response = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, objectMapper)
                .when()
                .post();

        response.then().log().all();

        // 1. Get raw YAML string
        String yamlString = response.then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // 2. Parse the envelope with Jackson (using YAMLFactory inside YAMLMapper)
        com.fasterxml.jackson.databind.JsonNode root = objectMapper
                .getMapper()                         // expose the Jackson mapper inside YAMLMapper
                .readTree(yamlString);

        // 3. Extract the "body" node and convert it to TokenDTO
        token = objectMapper.getMapper()
                .treeToValue(root.get("body"), TokenDTO.class);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {
        token = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("username", token.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }
}
