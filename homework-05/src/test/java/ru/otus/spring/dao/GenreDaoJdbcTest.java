package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long ID_1 = 1;
    private static final long ID_2 = 2;
    private static final long ID_3 = 3;
    private static final String NEW_NAME = "NEW_NAME";
    private static final String UNKNOWN = "UNKNOWN";

    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenresCount() {
        int actualGenresCount = genreDao.getAll().size();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("возвращать ожидаемый жанр по id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(ID_1, UNKNOWN);
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {
        assertThatCode(() -> genreDao.getById(ID_2))
                .doesNotThrowAnyException();

        genreDao.deleteById(ID_2);

        assertThatThrownBy(() -> genreDao.getById(ID_2)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        Genre expectedGenre1 = new Genre(ID_1, UNKNOWN);
        Genre expectedGenre2 = new Genre(ID_2, NEW_NAME);
        List<Genre> actualGenreList = genreDao.getAll();
        assertThat(actualGenreList).containsExactlyInAnyOrder(expectedGenre1, expectedGenre2);
    }

    @DisplayName("обновлять имя заданного жанра по его id")
    @Test
    void shouldCorrectUpdateGenreNameById() {
        String UPDATED_NAME = "UPDATED_NAME";
        Genre expectedGenre = new Genre(ID_1, UPDATED_NAME);
        assertThatCode(() -> genreDao.update(expectedGenre)).doesNotThrowAnyException();
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("создавать новый жанр")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void shouldCorrectCreateNewGenre() {
        Genre expectedGenre = new Genre(ID_3, UNKNOWN);
        assertThatCode(() -> genreDao.insert(expectedGenre)).doesNotThrowAnyException();
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
}
