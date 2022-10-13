package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final long ID = 1;
    private static final String BOOK_NAME = "book1";
    private static final String UNKNOWN = "UNKNOWN";

    @Autowired
    private BookDaoJdbc bookDao;

    @DisplayName("возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBooksCount = bookDao.getAll().size();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("возвращать ожидаемую книгу по id")
    @Test
    void shouldReturnExpectedBookById() {
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID, BOOK_NAME, author, genre);
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("удалять заданную книгу по его id")
    @Test
    void shouldCorrectDeleteBookById() {
        assertThatCode(() -> bookDao.getById(ID))
                .doesNotThrowAnyException();

        bookDao.deleteById(ID);

        assertThatThrownBy(() -> bookDao.getById(ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBooksList() {
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID, BOOK_NAME, author, genre);
        List<Book> actualBookList = bookDao.getAll();
        assertThat(actualBookList).containsExactlyInAnyOrder(expectedBook);
    }

    @DisplayName("обновлять имя заданной книги по его id")
    @Test
    void shouldCorrectUpdateBookNameById() {
        String UPDATED_NAME = "UPDATED_NAME";
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID, UPDATED_NAME, author, genre);
        assertThatCode(() -> bookDao.update(expectedBook)).doesNotThrowAnyException();
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("создавать новую книгу")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void shouldCorrectCreateNewBook() {
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID + 1, BOOK_NAME, author, genre);
        assertThatCode(() -> bookDao.insert(expectedBook)).doesNotThrowAnyException();
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("создавать новую книгу по имени")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void shouldCorrectCreateNewBookByName() {
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID + 1, UNKNOWN, author, genre);
        assertThatCode(() -> bookDao.insert(UNKNOWN)).doesNotThrowAnyException();
        Book actualBook = bookDao.getByName(expectedBook.getName());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("поменять автора у книги")
    @Test
    void shouldUpdateAuthorForBook() {
        String newAuthorName = "NEW_NAME";
        Author author = new Author(ID + 1, newAuthorName, newAuthorName);
        Genre genre = new Genre(ID, UNKNOWN);
        Book expectedBook = new Book(ID, BOOK_NAME, author, genre);
        assertThatCode(() -> bookDao.updateBookAuthor(ID, ID + 1)).doesNotThrowAnyException();
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("поменять жанр у книги")
    @Test
    void shouldUpdateGenreForBook() {
        String newGenreName = "NEW_NAME";
        Author author = new Author(ID, UNKNOWN, UNKNOWN);
        Genre genre = new Genre(ID + 1, newGenreName);
        Book expectedBook = new Book(ID, BOOK_NAME, author, genre);
        assertThatCode(() -> bookDao.updateBookGenre(ID, ID + 1)).doesNotThrowAnyException();
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }
}
