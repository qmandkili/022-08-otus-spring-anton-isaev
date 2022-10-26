package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public void createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreRepository.save(genre);
    }

    @Override
    public Genre getGenreById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public void updateGenre(long id, String name) {
        Genre genre = getGenreById(id);
        genre.setName(name);
        genreRepository.save(genre);
    }

    @Transactional
    @Override
    public void deleteGenre(long id) {
        genreRepository.deleteById(id);
    }
}
