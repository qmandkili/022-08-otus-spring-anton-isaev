package ru.otus.spring.repositories;

import ru.otus.spring.domain.Author;

import java.util.List;

public interface AuthorRepository {

    Author save(Author author);
    Author findById(long id);

    List<Author> findAll();
    List<Author> findByName(String firstName, String secondName);
    void deleteById(long id);
}
