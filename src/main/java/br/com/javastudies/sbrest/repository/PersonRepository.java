package br.com.javastudies.sbrest.repository;

import br.com.javastudies.sbrest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> { }
