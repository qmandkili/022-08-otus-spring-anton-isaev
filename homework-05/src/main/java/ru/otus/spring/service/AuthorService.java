package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

public interface AuthorService {

    void createAuthor(String firstName, String secondName);

    Author getAuthorById(long id);

    int updateAuthor(long id, String firstName, String secondName);

    void deleteAuthor(long id);
}
