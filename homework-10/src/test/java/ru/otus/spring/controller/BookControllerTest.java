package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.rest.controller.BookController;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;


    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<Book> books = List.of(new Book(1, "Book1", new Author(), new Genre(), List.of()),
                new Book(2, "Book2", new Author(), new Genre(), List.of()));
        when(bookService.getAllBooks()).thenReturn(books);
        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDtoWithoutComments).collect(Collectors.toList());

        ResultActions resultActions = mvc.perform(get("/api/books"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<AuthorDto> returnAuthors = getBooksDto(objectMapper.readValue(contentAsString, List.class));
        Assertions.assertEquals(expectedResult, returnAuthors);
    }

    @Test
    void shouldReturnCorrectBookByIdInPath() throws Exception {
        var book = new Book(1, "Book1", new Author(), new Genre(), new ArrayList<>());
        when(bookService.getBookById(1)).thenReturn(book);
        var expectedResult = BookDto.toDto(book);

        MvcResult result = mvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        BookDto returnBook = objectMapper.readValue(contentAsString, BookDto.class);
        Assertions.assertEquals(expectedResult, returnBook);
        verify(bookService).getBookById(book.getId());
    }

    @Test
    void shouldCorrectUpdateBook() throws Exception {
        var book = new Book(1, "Book1", new Author(), new Genre(), new ArrayList<>());

        doNothing().when(bookService).updateBook(book.getId(), book);
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(book);
        mvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCorrectCreateBook() throws Exception {
        var book = new Book(1, "Book1", new Author(), new Genre(), new ArrayList<>());

        doNothing().when(bookService).createBook( book);
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(book);
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isOk());
        verify(bookService).deleteBook(1L);
    }

    private List<BookDto> getBooksDto(List<LinkedHashMap> books) {
        List<BookDto> resultBooks = new ArrayList<>();
        for (LinkedHashMap b : books) {
            BookDto book = new BookDto();
            book.setId(((Integer) b.get("id")).longValue());
            book.setName((String) b.get("name"));
            AuthorDto author = getAuthorDto((LinkedHashMap) b.get("author"));
            book.setAuthor(author);
            GenreDto genre = getGenreDto((LinkedHashMap) b.get("genre"));
            book.setGenre(genre);
            resultBooks.add(book);
        }
        return resultBooks;
    }

    private AuthorDto getAuthorDto(LinkedHashMap authorMap) {
        AuthorDto author = new AuthorDto();
        author.setId(((Integer) authorMap.get("id")).longValue());
        author.setFirstName((String) authorMap.get("firstName"));
        author.setSecondName((String) authorMap.get("secondName"));
        return author;
    }

    private GenreDto getGenreDto(LinkedHashMap genreMap) {
        GenreDto genre = new GenreDto();
        genre.setId(((Integer) genreMap.get("id")).longValue());
        genre.setName((String) genreMap.get("name"));
        return genre;
    }
}
