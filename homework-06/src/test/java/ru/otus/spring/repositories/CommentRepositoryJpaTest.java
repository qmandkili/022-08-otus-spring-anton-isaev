package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository для работы с комментариями")
@DataJpaTest
@Import({CommentRepositoryJpa.class})
public class CommentRepositoryJpaTest {

    private static final long ID_1 = 1;
    private static final String COMMENT_TEXT = "COMMENT_TEXT";
    private static final String NEW_COMMENT_TEXT = "NEW_COMMENT_TEXT";
    private static final int EXPECTED_NUMBER_OF_COMMENTS = 2;

    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен корректно сохранять комментарий в бд")
    @Test
    void shouldSaveComment() {
        Book book = em.find(Book.class, ID_1);
        Comment comment = new Comment();
        comment.setText(COMMENT_TEXT);
        comment.setBook(book);
        comment = commentRepository.save(comment);

        assertThat(comment.getId()).isGreaterThan(0);

        Comment actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(comment);
    }

    @DisplayName(" должен удалять комментарий по его Id")
    @Test
    void shouldDeleteBookNameById() {
        commentRepository.deleteById(ID_1);
        Comment deletedComment = em.find(Comment.class, ID_1);
        assertThat(deletedComment).isNull();
    }
}
