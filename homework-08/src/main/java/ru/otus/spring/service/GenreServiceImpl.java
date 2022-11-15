package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final SequenceGenerator generator;

    @Transactional
    @Override
    public void createGenre(String name) {
        Genre genre = new Genre();
        genre.setId(generator.generateSequence(Genre.SEQUENCE_NAME));
        genre.setName(name);
        genreRepository.save(genre);
    }

    @Override
    public Genre getGenreById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
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

    @Override
    public void deleteGenre(long id) {
        genreRepository.deleteById(id);
    }
}
