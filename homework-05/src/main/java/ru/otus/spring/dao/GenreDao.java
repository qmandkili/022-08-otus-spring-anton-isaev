package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreDao {

    long insert(Genre genre);

    Genre getById(long id);

    List<Genre> getAll();

    void deleteById(long id);

    int update(Genre genre);
}
