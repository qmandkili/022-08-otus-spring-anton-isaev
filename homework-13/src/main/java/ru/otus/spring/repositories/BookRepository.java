package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    //@PostFilter("hasPermission(filterObject, 'READ')")
    @EntityGraph(value = "book-author-genre-graph")
    List<Book> findAll();

    /*@PostAuthorize("hasPermission(returnObject, 'READ')")
    Book getById(Integer id);

    @SuppressWarnings("unchecked")
    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    Book save(@Param("book") Book book);

    @PreAuthorize("hasPermission(#book, 'DELETE')")
    void delete(@Param("book") Book book);*/
}
