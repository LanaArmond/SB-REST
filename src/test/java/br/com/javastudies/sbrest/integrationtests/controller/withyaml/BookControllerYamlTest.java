package br.com.javastudies.sbrest.integrationtests.controller.withyaml;

import br.com.javastudies.sbrest.config.TestConfigs;
import br.com.javastudies.sbrest.integrationtests.controller.withyaml.Mapper.YAMLMapper;
import br.com.javastudies.sbrest.integrationtests.dto.BookDTO;
import br.com.javastudies.sbrest.integrationtests.dto.wrapper.xml_yaml.PagedModelBook;
import br.com.javastudies.sbrest.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();
        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/book")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdBook = given().config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                        )).spec(specification)
            .contentType(MediaType.APPLICATION_YAML_VALUE)
            .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(book,yamlMapper)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
            .extract()
                .body()
                     .as(BookDTO.class, yamlMapper);

        book = createdBook;

        assertNotNull(book.getId());
        assertNotNull(createdBook.getId());

        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Nigel Poulton", createdBook.getAuthor());
        assertEquals("Docker Deep Dive", createdBook.getTitle());
        assertEquals(55.99, createdBook.getPrice());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setTitle("Docker Deep Dive - Updated");

        // Usa a specification do teste anterior

        var createdBook = given().config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                        )).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(book, yamlMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(BookDTO.class, yamlMapper);

        book = createdBook;

        assertNotNull(book.getId());
        assertNotNull(createdBook.getId());

        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Nigel Poulton", createdBook.getAuthor());
        assertEquals("Docker Deep Dive - Updated", createdBook.getTitle());
        assertEquals(55.99, createdBook.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        // Usa a specification do teste anterior

        var createdBook = given().config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                        )).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(BookDTO.class, yamlMapper);

        book = createdBook;

        assertNotNull(book.getId());
        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Docker Deep Dive - Updated", createdBook.getTitle());
        assertEquals("Nigel Poulton", createdBook.getAuthor());
        assertEquals(55.99, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {

        // Usa a specification do teste anterior

        given(specification)
                .pathParam("id", book.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        // Usa a specification do teste anterior

        var response = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PagedModelBook.class, yamlMapper);

        List<BookDTO> books = response.getContent();

        BookDTO bookZero = books.get(0);
        book = bookZero;

        assertNotNull(bookZero.getId());
        assertNotNull(bookZero.getAuthor());
        assertNotNull(bookZero.getTitle());
        assertNotNull(bookZero.getLaunchDate());
        assertNotNull(bookZero.getPrice());

        assertTrue(bookZero.getId() > 0);

        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookZero.getTitle());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookZero.getAuthor());
        assertEquals(54.00, bookZero.getPrice());

        BookDTO bookFour =books.get(4);
        book = bookFour;

        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getAuthor());
        assertNotNull(bookFour.getTitle());
        assertNotNull(bookFour.getLaunchDate());
        assertNotNull(bookFour.getPrice());

        assertTrue(bookFour.getId() > 0);

        assertEquals("Domain Driven Design", bookFour.getTitle());
        assertEquals("Eric Evans", bookFour.getAuthor());
        assertEquals(92.00, bookFour.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}