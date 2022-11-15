package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private SequenceGenerator generator;
    @InjectMocks
    private CommentServiceImpl commentService;

    private static final long ID = 1;
    private static final String TEXT = "TEXT";

    @Test
    public void createCommentHappyPath() {
        Comment comment = new Comment();
        comment.setId(ID);
        comment.setText(TEXT);
        when(generator.generateSequence(any())).thenReturn(ID);
        commentService.createComment(TEXT);

        verify(commentRepository).save(comment);
        verify(generator).generateSequence(any());
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
}