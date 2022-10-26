package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

import java.util.List;

public interface GenreService {

    void createGenre(String name);

    Genre getGenreById(long id);

    List<Genre> findAll();

    void updateGenre(long id, String name);

    void deleteGenre(long id);
}
