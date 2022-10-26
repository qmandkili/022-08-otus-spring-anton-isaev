package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository для работы с жанрами")
@DataJpaTest
@Import({GenreRepositoryJpa.class})
public class GenreRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_GENRES = 2;
    private static final long FIRST_GENRE_ID = 1;
    private static final String FIRST_GENRE_NAME = "FIRST_GENRE_NAME";
    private static final String NEW_GENRE_NAME = "NEW_GENRE_NAME";

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен корректно сохранять жанр в бд")
    @Test
    void shouldSaveGenre() {
        Genre Genre = new Genre();
        Genre.setName(NEW_GENRE_NAME);
        genreRepository.save(Genre);

        assertThat(Genre.getId()).isGreaterThan(0);
        Genre actualGenre = em.find(Genre.class, Genre.getId());
        assertThat(actualGenre).isNotNull().matches(a -> a.getName().equals(NEW_GENRE_NAME));
    }

    @DisplayName("должен находить жанр по его id")
    @Test
    void shouldFindGenreById() {
        Genre optionalActualGenre = genreRepository.findById(FIRST_GENRE_ID);
        Genre expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(optionalActualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenreList() {
        List<Genre> Genres = genreRepository.findAll();
        assertThat(Genres).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
                .allMatch(Genre -> !Genre.getName().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужном жанре по его имени")
    @Test
    void shouldFindGenreByName() {
        Genre firstGenre = em.find(Genre.class, FIRST_GENRE_ID);
        List<Genre> Genres = genreRepository.findByName(FIRST_GENRE_NAME);
        assertThat(Genres).containsOnlyOnce(firstGenre);
    }

    @DisplayName(" должен удалять нужный жанр по ее Id")
    @Test
    void shouldDeleteGenreNameById() {
        em.clear();
        genreRepository.deleteById(FIRST_GENRE_ID);
        Genre deletedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(deletedGenre).isNull();
    }
}
