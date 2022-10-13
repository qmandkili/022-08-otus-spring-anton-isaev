package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

public interface GenreService {

    void createGenre(String name);

    Genre getGenreById(long id);

    int updateGenre(long id, String name);

    void deleteGenre(long id);
}
