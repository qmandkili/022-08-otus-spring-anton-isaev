package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository для работы с авторами")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
public class AuthorRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 2;
    private static final long FIRST_AUTHOR_ID = 1;
    private static final String AUTHOR_FIRST_NAME = "FIRST_NAME";
    private static final String AUTHOR_SECOND_NAME = "SECOND_NAME";
    private static final String NEW_AUTHOR_FIRST_NAME = "NEW_FIRST_NAME";
    private static final String NEW_AUTHOR_SECOND_NAME = "NEW_SECOND_NAME";

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен корректно сохранять автора в бд")
    @Test
    void shouldSaveAuthor() {
        Author author = new Author();
        author.setFirstName(AUTHOR_FIRST_NAME);
        author.setSecondName(AUTHOR_SECOND_NAME);
        authorRepository.save(author);

        assertThat(author.getId()).isGreaterThan(0);
        Author actualAuthor = em.find(Author.class, author.getId());
        assertThat(actualAuthor).isNotNull().matches(a -> a.getFirstName().equals(AUTHOR_FIRST_NAME)
                        && a.getSecondName().equals(AUTHOR_SECOND_NAME));
    }

    @DisplayName("должен находить автора по его id")
    @Test
    void shouldFindAuthorById() {
        Author optionalActualAuthor = authorRepository.findById(FIRST_AUTHOR_ID);
        Author expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(author -> !author.getFirstName().equals("")
                        && !author.getSecondName().equals(""));
    }

    @DisplayName(" должен загружать информацию о нужном авторе по его имени")
    @Test
    void shouldFindAuthorByName() {
        Author firstAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        List<Author> authors = authorRepository.findByName(AUTHOR_FIRST_NAME, AUTHOR_SECOND_NAME);
        assertThat(authors).containsOnlyOnce(firstAuthor);
    }

    @DisplayName(" должен удалять нужную книгу по ее Id")
    @Test
    void shouldDeleteAuthorNameById() {
        em.clear();
        authorRepository.deleteById(FIRST_AUTHOR_ID);
        Author deletedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(deletedAuthor).isNull();
    }
}
