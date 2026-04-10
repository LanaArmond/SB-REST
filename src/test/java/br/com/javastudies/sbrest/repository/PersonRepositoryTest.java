package br.com.javastudies.sbrest.repository;

import br.com.javastudies.sbrest.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.javastudies.sbrest.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // Integra Spring Framework com JUNIT5
@DataJpaTest // Configura o teste para trabalhar com JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Impede que o DataJPATests substitua a config do banco do projeto pelo embutido
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void disablePerson() {

        Pageable pageable = PageRequest.of(0, 12, Sort.Direction.ASC, "firstName");
        person = repository.findPeopleByName("ariana", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Mariana", person.getFirstName());
        assertEquals("Costa", person.getLastName());
        assertEquals("Brasil", person.getAddress());
        assertEquals("Female", person.getGender());
        Assertions.assertTrue(person.getEnabled());


    }

    @Order(2)
    @Test
    void findPeopleByName() {

        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Mariana", person.getFirstName());
        assertEquals("Costa", person.getLastName());
        assertEquals("Brasil", person.getAddress());
        assertEquals("Female", person.getGender());
        Assertions.assertFalse(person.getEnabled());
    }
}