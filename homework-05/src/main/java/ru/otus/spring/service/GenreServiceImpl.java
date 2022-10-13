package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Transactional
    public void createGenre(String name) {
        Genre genre = new Genre(name);
        genreDao.insert(genre);
    }

    public Genre getGenreById(long id) {
        return genreDao.getById(id);
    }

    @Transactional
    public int updateGenre(long id, String name) {
        Genre genre = genreDao.getById(id);
        genre.setName(name);
        return genreDao.update(genre);
    }

    @Transactional
    public void deleteGenre(long id) {
        genreDao.deleteById(id);
    }
}
