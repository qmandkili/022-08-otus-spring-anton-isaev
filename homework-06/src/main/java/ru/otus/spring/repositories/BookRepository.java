package ru.otus.spring.repositories;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    Book findById(long id);

    List<Book> findAll();

    List<Book> findByName(String name);

    void deleteById(long id);
}
