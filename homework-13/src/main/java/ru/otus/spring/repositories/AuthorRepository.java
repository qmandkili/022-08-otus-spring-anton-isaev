package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    /*@PostFilter("hasPermission(filterObject, 'READ')")
    @EntityGraph(value = "book-author-graph")
    List<Author> findAll();*/

    @EntityGraph(value = "book-author-graph")
    Optional<Author> findById(long id);

    /*@PostAuthorize("hasPermission(returnObject, 'READ')")
    Author getById(Integer id);

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#author, 'WRITE')")
    Author save(@Param("author") Author author);

    @PreAuthorize("hasPermission(#author, 'DELETE')")
    void delete(@Param("author") Author author);*/
}
