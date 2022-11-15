package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;
    @Mock
    private SequenceGenerator generator;
    @InjectMocks
    private GenreServiceImpl genreService;

    private static final long ID = 1;
    private static final String NAME = "NAME";
    private static final String NEW_NAME = "NEW_NAME";

    @Test
    public void createGenreHappyPath() {
        Genre Genre = new Genre();
        Genre.setName(NAME);
        Genre.setId(ID);
        when(generator.generateSequence(any())).thenReturn(ID);
        genreService.createGenre(NAME);

        verify(genreRepository).save(Genre);
        verify(generator).generateSequence(any());
    }

    @Test
    public void getGenreByIdHappyPath() {
        Genre genre = mock(Genre.class);
        when(genreRepository.findById(ID)).thenReturn(Optional.of(genre));
        Genre actualGenre = genreService.getGenreById(ID);

        assertThat(actualGenre).isEqualTo(genre);
        verify(genreRepository).findById(ID);
    }

    @Test
    public void getAllGenresHappyPath() {
        List<Genre> genres = List.of(mock(Genre.class), mock(Genre.class));
        when(genreRepository.findAll()).thenReturn(genres);
        List<Genre> actualGenres = genreService.findAll();

        assertThat(actualGenres.size()).isGreaterThan(1);
        verify(genreRepository).findAll();
    }

    @Test
    public void updateGenreHappyPath() {
        Genre genre = mock(Genre.class);
        when(genreRepository.findById(ID)).thenReturn(Optional.of(genre));
        genre.setName(NEW_NAME);
        genreService.updateGenre(ID, NAME);
    }

    @Test
    public void deleteGenreHappyPath() {
        genreService.deleteGenre(ID);
        verify(genreRepository).deleteById(ID);
    }
}