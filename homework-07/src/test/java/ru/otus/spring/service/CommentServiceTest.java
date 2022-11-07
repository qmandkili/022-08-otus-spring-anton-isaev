package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    private static final long ID = 1;
    private static final String TEXT = "TEXT";

    @Test
    public void createCommentHappyPath() {
        Comment comment = new Comment();
        comment.setText(TEXT);
        commentService.createComment(TEXT);

        verify(commentRepository).save(comment);
    }

    @Test
    public void getCommentByIdHappyPath() {
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));
        Comment actualComment = commentService.getCommentById(ID);

        assertThat(actualComment).isEqualTo(comment);
        verify(commentRepository).findById(ID);
    }

    @Test
    public void updateTextByIdHappyPath() {
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));
        comment.setText(TEXT);
        commentService.updateTextById(ID, TEXT);
    }

    @Test
    public void deleteCommentHappyPath() {
        commentService.deleteComment(ID);

        verify(commentRepository).deleteById(ID);
    }

    @Test
    public void createBookCommentHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        Comment comment = new Comment();
        comment.setText(TEXT);
        comment.setBook(book);
        when(commentRepository.save(comment)).thenReturn(comment);
        commentService.createBookComment(ID, TEXT);

        verify(bookRepository).findById(ID);
        verify(commentRepository).save(comment);
    }

    @Test
    public void addBookCommentHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));
        commentService.addBookComment(ID, ID);
    }
}