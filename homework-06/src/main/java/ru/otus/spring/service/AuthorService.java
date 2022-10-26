package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.List;

public interface AuthorService {

    void createAuthor(String firstName, String secondName);

    Author getAuthorById(long id);

    List<Author> findAll();

    void updateAuthor(long id, String firstName, String secondName);

    void deleteAuthor(long id);
}
