package ru.otus.spring.repositories;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreRepository {

    Genre save(Genre genre);
    Genre findById(long id);

    List<Genre> findAll();
    List<Genre> findByName(String name);
    void deleteById(long id);
}
