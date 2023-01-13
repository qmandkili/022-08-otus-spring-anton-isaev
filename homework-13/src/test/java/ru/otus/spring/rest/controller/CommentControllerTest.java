package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.*;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.security.AuthProvider;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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
    @MockBean
    private AuthProvider authProvider;

    @Test
    void shouldReturnCorrectCommentsList() throws Exception {
        var author = new Author(1L, "Author", "Author", null, 1);
        var genre = new Genre(1L, "Genre", 1);
        var book = new Book(1L, "Book1", author, genre, null, 1);
        when(bookService.getBookById(1)).thenReturn(book);
        List<Comment> comments = List.of(
                new Comment(1L, "Comment1", book, 1),
                new Comment(2L, "Comment2", book, 1));
        book.setComments(comments);
        List<CommentDto> expectedResult = book.getComments().stream()
                .map(CommentDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/comments/" + book.getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("comments", hasSize(2)))
                .andExpect(model().attribute("comments", expectedResult))
                .andExpect(model().attribute("book", BookDto.toDto(book)));
        verify(bookService).getBookById(1);
    }

    @Test
    void shouldReturnCorrectCommentEditPageByIdInPath() throws Exception {
        var author = new Author(1L, "Author", "Author", null, 1);
        var genre = new Genre(1L, "Genre", 1);
        var book = new Book();
        book.setGenre(genre);
        book.setAuthor(author);
        var comment = new Comment(1L, "Comment1", book, 1);
        when(commentService.getCommentById(comment.getId())).thenReturn(comment);

        var expectedResult = CommentDto.toDto(comment);

        mvc.perform(get("/comments/edit?id={id}", comment.getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("comment", expectedResult));
        verify(commentService).getCommentById(comment.getId());
    }

    @Test
    void shouldCorrectUpdateCommentName() throws Exception {
        var comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Comment1");
        var book = new BookDto();
        book.setId(1L);
        comment.setBook(book);
        doNothing().when(commentService).updateTextById(comment.getId(), comment.getText());

        mvc.perform(post("/comments/edit?id={id}&text={text}&book.id={book.id}",
                        comment.getId(), comment.getText(), comment.getBook().getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(authenticated())
                .andExpect(status().isFound());
        verify(commentService).updateTextById(comment.getId(), comment.getText());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        var author = new Author(1L, "Author", "Author", null, 1);
        var genre = new Genre(1L, "Genre", 1);
        var book = new Book(1L, "Book1", author, genre, null, 1);
        var comment = new Comment();
        comment.setBook(book);
        when(bookService.getBookById(book.getId())).thenReturn(book);
        mvc.perform(get("/comments/create/" + book.getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(model().attributeExists("comment"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
        verify(bookService).getBookById(book.getId());
    }

    @Test
    void shouldCorrectSaveNewComment() throws Exception {
        var comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Comment1");
        var book = new BookDto();
        book.setId(1L);
        comment.setBook(book);
        doNothing().when(commentService).createBookComment(comment.getBook().getId(), comment.getText());

        mvc.perform(post("/comments/create?book.id={id}&text={text}",
                        comment.getBook().getId(), comment.getText())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(authenticated())
                .andExpect(status().isFound());
        verify(commentService).createBookComment(comment.getBook().getId(), comment.getText());
    }

    @Test
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(post("/comments/delete/1")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(authenticated())
                .andExpect(status().isFound());
        verify(commentService).deleteComment(1L);
    }

    @Test
    void commentsUnauthenticated() throws Exception {
        mvc.perform(get("/comments/1"))
                .andExpect(unauthenticated());
        mvc.perform(get("/comments/edit?id=1"))
                .andExpect(unauthenticated());
        mvc.perform(post("/comments/edit?id=1&text=text&book.id=1"))
                .andExpect(unauthenticated());
        mvc.perform(get("/comments/create/1"))
                .andExpect(unauthenticated());
        mvc.perform(post("/comments/create?book.id=1&text=text"))
                .andExpect(unauthenticated());
        mvc.perform(post("/comments/delete/1"))
                .andExpect(unauthenticated());
    }
}
