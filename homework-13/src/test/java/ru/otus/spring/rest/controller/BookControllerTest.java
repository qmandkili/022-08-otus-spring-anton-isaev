package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Role;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.security.AuthProvider;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private BookService bookService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthProvider authProvider;

    @Test
    void shouldListAllBooks() throws Exception {
        var author = new Author(1L, "Author1", "Author1", null, 1);
        var genre = new Genre(1L, "Genre1", 1);
        List<Book> books = List.of(
                new Book(1L, "Book1", author, genre, null, 1),
                new Book(2L, "Book2", author, genre, null, 1));
        when(bookService.getAllBooks()).thenReturn(books);
        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDtoWithoutComments).collect(Collectors.toList());
        when(bookService.getAllBooks()).thenReturn(books);
        mvc.perform(get("/books")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", expectedResult));
    }

    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var author = new Author(1L, "Author1", "Author1", null, 1);
        var genre = new Genre(1L, "Genre1", 1);
        List<Book> books = List.of(
                new Book(1L, "Book1", author, genre, null, 1),
                new Book(2L, "Book2", author, genre, null, 1));
        when(bookService.getAllBooks()).thenReturn(books);
        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDtoWithoutComments).collect(Collectors.toList());

        mvc.perform(get("/books")
                        .with(user("admin").password("password").authorities(Role.ADMIN).authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", expectedResult));
    }

    @Test
    void shouldReturnCorrectBookEditPageByIdInPath() throws Exception {
        var author = new Author(1L, "Author", "Author", null, 1);
        var genre = new Genre(1L, "Genre", 1);
        var book = new Book(1L, "Book1", author, genre, null, 1);
        List<Author> authors = List.of(
                new Author(1L, "Author1", "Author1", null, 1),
                new Author(2L, "Author2", "Author2", null, 1));
        List<Genre> genres = List.of(
                new Genre(1L, "Genre1", 1),
                new Genre(2L, "Genre2", 1));
        when(genreService.findAll()).thenReturn(genres);
        when(bookService.getBookById(1)).thenReturn(book);
        when(authorService.findAll()).thenReturn(authors);

        var expectedBookResult = BookDto.toDtoWithoutComments(book);
        var expectedAuthorResult = authors.stream()
                .map(AuthorDto::toDto).collect(Collectors.toList());
        var expectedGenreResult = genres.stream()
                .map(GenreDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/books/edit?id={id}", book.getId())
                .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", expectedBookResult))
                .andExpect(model().attribute("authors", expectedAuthorResult))
                .andExpect(model().attribute("genres", expectedGenreResult));
        verify(bookService).getBookById(book.getId());
        verify(genreService).findAll();
        verify(authorService).findAll();
    }

    @Test
    void shouldCorrectUpdateBookName() throws Exception {
        var author = new Author();
        author.setId(1L);
        var genre = new Genre();
        genre.setId(1L);
        var book = new Book(1L, "Book1", author, genre, null, 1);
        doNothing().when(bookService).updateBook(book.getId(), book.getName());
        doNothing().when(bookService).addBookAuthor(book.getId(), book.getAuthor().getId());
        doNothing().when(bookService).addBookGenre(book.getId(), book.getGenre().getId());


        mvc.perform(post("/books/edit?id={id}&name={name}&author.id={author.id}&genre.id={genre.id}",
                        book.getId(), book.getName(), book.getAuthor().getId(),
                        book.getGenre().getId()).with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(bookService).updateBook(book.getId(), book.getName());
        verify(bookService).addBookAuthor(book.getId(), book.getAuthor().getId());
        verify(bookService).addBookGenre(book.getId(), book.getGenre().getId());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        List<Author> authors = List.of(
                new Author(1L, "Author1", "Author1", null, 1),
                new Author(2L, "Author2", "Author2", null, 1));
        List<Genre> genres = List.of(
                new Genre(1L, "Genre1", 1),
                new Genre(2L, "Genre2", 1));
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);
        var expectedAuthorResult = authors.stream()
                .map(AuthorDto::toDto).collect(Collectors.toList());
        var expectedGenreResult = genres.stream()
                .map(GenreDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/books/create")
                .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", expectedAuthorResult))
                .andExpect(model().attribute("genres", expectedGenreResult))
                .andExpect(status().isOk());
        verify(genreService).findAll();
        verify(authorService).findAll();
    }

    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        var author = new Author();
        author.setId(1L);
        var genre = new Genre();
        genre.setId(1L);

        var book = new Book(null, "Book1", author, genre, null, 1);
        doNothing().when(bookService).createBook(book);
        when(authorService.getAuthorById(book.getAuthor().getId())).thenReturn(author);
        when(genreService.getGenreById(book.getAuthor().getId())).thenReturn(genre);

        mvc.perform(post("/books/create?name={name}&author.id={author.id}&genre.id={genre.id}",
                        book.getName(), book.getAuthor().getId(), book.getGenre().getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(bookService).createBook(book);
        verify(authorService).getAuthorById(book.getAuthor().getId());
        verify(genreService).getGenreById(book.getGenre().getId());
    }

    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(post("/books/delete/1")
                .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(bookService).deleteBook(1);
    }
}
