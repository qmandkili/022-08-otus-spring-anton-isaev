package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private BookService bookService;
    @MockBean
    private UserService userService;

    @Test
    void shouldReturnCorrectCommentsList() throws Exception {
        var genre = new Genre();
        var author = new Author();
        var book = new Book(1, "Book1", author, genre, null);
        when(bookService.getBookById(1)).thenReturn(book);
        List<Comment> comments = List.of(
                new Comment(1L, "Comment1", book),
                new Comment(2L, "Comment2", book));
        book.setComments(comments);
        List<CommentDto> expectedResult = book.getComments().stream()
                .map(CommentDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/comments/" + book.getId())
                        .with(user("admin").password("password")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("comments", hasSize(2)))
                .andExpect(model().attribute("comments", expectedResult))
                .andExpect(model().attribute("book", BookDto.toDto(book)));
        verify(bookService).getBookById(1);
    }

    @Test
    void shouldReturnCorrectCommentEditPageByIdInPath() throws Exception {
        var book = new Book();
        book.setGenre(new Genre());
        book.setAuthor(new Author());
        var comment = new Comment(1, "Comment1", book);
        when(commentService.getCommentById(comment.getId())).thenReturn(comment);

        var expectedResult = CommentDto.toDto(comment);

        mvc.perform(get("/comments/edit?id={id}", comment.getId())
                        .with(user("admin").password("password")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("comment", expectedResult));
        verify(commentService).getCommentById(comment.getId());
    }

    @Test
    void shouldCorrectUpdateCommentName() throws Exception {
        var comment = new CommentDto();
        comment.setId(1);
        comment.setText("Comment1");
        var book = new BookDto();
        book.setId(1);
        comment.setBook(book);
        doNothing().when(commentService).updateTextById(comment.getId(), comment.getText());

        mvc.perform(post("/comments/edit?id={id}&text={text}&book.id={book.id}",
                        comment.getId(), comment.getText(), comment.getBook().getId())
                        .with(user("admin").password("password")))
                .andExpect(status().isFound());
        verify(commentService).updateTextById(comment.getId(), comment.getText());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        var genre = new Genre();
        var author = new Author();
        var book = new Book(1, "Book1", author, genre, null);
        var comment = new Comment();
        comment.setBook(book);
        when(bookService.getBookById(book.getId())).thenReturn(book);
        mvc.perform(get("/comments/create/" + book.getId())
                        .with(user("admin").password("password")))
                .andExpect(model().attributeExists("comment"))
                .andExpect(status().isOk());
        verify(bookService).getBookById(book.getId());
    }

    @Test
    void shouldCorrectSaveNewComment() throws Exception {
        var comment = new CommentDto();
        comment.setId(1);
        comment.setText("Comment1");
        var book = new BookDto();
        book.setId(1);
        comment.setBook(book);
        doNothing().when(commentService).createBookComment(comment.getBook().getId(), comment.getText());

        mvc.perform(post("/comments/create?book.id={id}&text={text}",
                        comment.getBook().getId(), comment.getText())
                        .with(user("admin").password("password")))
                .andExpect(status().isFound());
        verify(commentService).createBookComment(comment.getBook().getId(), comment.getText());
    }

    @Test
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(post("/comments/delete/1")
                        .with(user("admin").password("password")))
                .andExpect(status().isFound());
        verify(commentService).deleteComment(1L);
    }
}
