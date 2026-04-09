package br.com.javastudies.sbrest.unittests.service;

import br.com.javastudies.sbrest.data.dto.BookDTO;
import br.com.javastudies.sbrest.exception.RequiredObjectIsNullException;
import br.com.javastudies.sbrest.model.Book;
import br.com.javastudies.sbrest.repository.BookRepository;
import br.com.javastudies.sbrest.service.BookService;
import br.com.javastudies.sbrest.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        // Teste HATEOAS findById
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/1") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/1") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Some Title1", result.getTitle());
    }

    @Test
    void create() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(any(Book.class))).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        // Teste HATEOAS findById
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/1") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/1") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Some Title1", result.getTitle());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> { service.create(null); }
        );
        String expectedMessage = "It is no allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        // Teste HATEOAS findById
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/1") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/1") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Some Title1", result.getTitle());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> { service.update(null); }
                );
        String expectedMessage = "It is no allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        // Teste 01
        var bookOne = books.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        // Teste HATEOAS findById
        assertNotNull(bookOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/1") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(bookOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(bookOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(bookOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(bookOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/1") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author1", bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(25D, bookOne.getPrice());
        assertEquals("Some Title1", bookOne.getTitle());

        // Teste 02
        var bookFour = books.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        // Teste HATEOAS findById
        assertNotNull(bookFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/4") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(bookFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(bookFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(bookFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(bookFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/4") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author4", bookFour.getAuthor());
        assertNotNull(bookFour.getLaunchDate());
        assertEquals(25D, bookFour.getPrice());
        assertEquals("Some Title4", bookFour.getTitle());

        // Teste 03
        var bookSeven = books.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        // Teste HATEOAS findById
        assertNotNull(bookSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/book/4") && link.getType().equals("GET")));

        // Teste HATEOAS findAll
        assertNotNull(bookSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/book") && link.getType().equals("GET")));

        // Teste HATEOAS create
        assertNotNull(bookSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/book") && link.getType().equals("POST")));

        // Teste HATEOAS update
        assertNotNull(bookSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/book") && link.getType().equals("PUT")));

        // Teste HATEOAS delete
        assertNotNull(bookSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/book/4") && link.getType().equals("DELETE")));

        // Teste de validade segundo o mocks.MockBook
        assertEquals("Some Author7", bookSeven.getAuthor());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals(25D, bookSeven.getPrice());
        assertEquals("Some Title7", bookSeven.getTitle());
    }
}