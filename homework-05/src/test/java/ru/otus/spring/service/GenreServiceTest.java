package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreServiceImpl genreService;

    private static final long ID = 1;
    private static final String NAME = "NAME";

    @Test
    public void createGenreHappyPath() {
        Genre genre = new Genre(NAME);
        genreService.createGenre(NAME);

        verify(genreDao).insert(genre);
    }

    @Test
    public void getGenreByIdHappyPath() {
        Genre genre = mock(Genre.class);
        when(genreDao.getById(ID)).thenReturn(genre);
        Genre actualGenre = genreService.getGenreById(ID);

        assertThat(actualGenre).isEqualTo(genre);
        verify(genreDao).getById(ID);
    }

    @Test
    public void updateGenreHappyPath() {
        Genre genre = mock(Genre.class);
        when(genreDao.getById(ID)).thenReturn(genre);
        when(genreDao.update(genre)).thenReturn(1);
        int updateResult = genreService.updateGenre(ID, NAME);

        assertThat(updateResult).isEqualTo(1);
        verify(genreDao).update(genre);
    }

    @Test
    public void deleteGenreHappyPath() {
        genreService.deleteGenre(ID);
        verify(genreDao).deleteById(ID);
    }
}
