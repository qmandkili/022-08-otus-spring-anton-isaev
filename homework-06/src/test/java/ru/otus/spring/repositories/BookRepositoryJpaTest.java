package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository для работы с книгами")
@DataJpaTest
@Import({BookRepositoryJpa.class, CommentRepositoryJpa.class})
public class BookRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 2;
    private static final long FIRST_BOOK_ID = 1;
    private static final String NEW_BOOK_TITLE = "new book";
    private static final String FIRST_BOOK_NAME = "FIRST_BOOK_NAME";
    private static final String FIRST_GENRE_NAME = "FIRST_GENRE_NAME";
    private static final String AUTHOR_FIRST_NAME = "FIRST_NAME";
    private static final String AUTHOR_SECOND_NAME = "SECOND_NAME";

    @Autowired
    private BookRepositoryJpa bookRepository;
    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен корректно сохранять книгу в бд")
    @Test
    void shouldSaveBook() {
        Author author = new Author();
        author.setFirstName(AUTHOR_FIRST_NAME);
        author.setSecondName(AUTHOR_SECOND_NAME);
        Genre genre = new Genre();
        genre.setName(FIRST_GENRE_NAME);
        Book book = new Book();
        book.setName(FIRST_BOOK_NAME);
        book.setAuthor(author);
        book.setGenre(genre);
        book = bookRepository.save(book);

        assertThat(book.getId()).isGreaterThan(0);

        Book actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().matches(b -> !b.getName().equals(""))
                .matches(b -> b.getAuthor() != null)
                .matches(b -> b.getGenre() != null);
    }

    @DisplayName("должен загружать информацию о нужной книге по её Id")
    @Test
    void shouldFindBookById() {
        Book optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalActualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг с информацией об авторе и жанре")
    @Test
    void shouldReturnCorrectBookListWithGenreAndAuthor() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> !book.getName().equals(""))
                .allMatch(book -> book.getGenre() != null)
                .allMatch(book -> book.getAuthor() != null);
    }

    @DisplayName(" должен загружать информацию о нужной книге по ее имени")
    @Test
    void shouldFindBookByName() {
        Book firstBook = em.find(Book.class, FIRST_BOOK_ID);
        List<Book> books = bookRepository.findByName(FIRST_BOOK_NAME);
        assertThat(books).containsOnlyOnce(firstBook);
    }

    @DisplayName(" должен удалять нужную книгу по ее Id")
    @Test
    void shouldDeleteBookNameById() {
        em.clear();
        commentRepository.deleteByBookId(FIRST_BOOK_ID);
        bookRepository.deleteById(FIRST_BOOK_ID);
        Book deletedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(deletedBook).isNull();
    }
}
