package br.com.javastudies.sbrest.repository;

import br.com.javastudies.sbrest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
