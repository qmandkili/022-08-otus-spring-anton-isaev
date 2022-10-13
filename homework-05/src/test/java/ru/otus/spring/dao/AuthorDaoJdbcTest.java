package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {

    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final long ID_1 = 1;
    private static final long ID_2 = 2;
    private static final long ID_3 = 3;
    private static final String NEW_NAME = "NEW_NAME";
    private static final String UNKNOWN = "UNKNOWN";

    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("возвращать ожидаемое количество авторов в БД")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        int actualBooksCount = authorDao.getAll().size();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("возвращать ожидаемого автора по id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(ID_1, UNKNOWN, UNKNOWN);
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        assertThatCode(() -> authorDao.getById(ID_2))
                .doesNotThrowAnyException();

        authorDao.deleteById(ID_2);

        assertThatThrownBy(() -> authorDao.getById(ID_2)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorsList() {
        Author expectedAuthor1 = new Author(ID_1, UNKNOWN, UNKNOWN);
        Author expectedAuthor2 = new Author(ID_2, NEW_NAME, NEW_NAME);
        List<Author> actualAuthorList = authorDao.getAll();
        assertThat(actualAuthorList).containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2);
    }

    @DisplayName("обновлять имя заданного автора по его id")
    @Test
    void shouldCorrectUpdateAuthorNameById() {
        String UPDATED_NAME = "UPDATED_NAME";
        Author expectedAuthor = new Author(ID_1, UPDATED_NAME, UPDATED_NAME);
        assertThatCode(() -> authorDao.update(expectedAuthor)).doesNotThrowAnyException();
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("создавать нового автора")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void shouldCorrectCreateNewAuthor() {
        Author expectedAuthor = new Author(ID_3, UNKNOWN, UNKNOWN);
        assertThatCode(() -> authorDao.insert(expectedAuthor)).doesNotThrowAnyException();
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}
