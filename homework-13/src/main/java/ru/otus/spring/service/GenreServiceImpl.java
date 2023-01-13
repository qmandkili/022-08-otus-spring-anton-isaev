package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreRepository.save(genre);
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') and returnObject.accessLevel <= authentication.principal.accessLevel")
    public Genre getGenreById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
    }

    @Override
    @PostFilter("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') " +
            "and filterObject.accessLevel <= authentication.principal.accessLevel")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateGenre(long id, String name) {
        Genre genre = getGenreById(id);
        genre.setName(name);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteGenre(long id) {
        genreRepository.deleteById(id);
    }
}