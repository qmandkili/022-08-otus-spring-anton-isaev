package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс BookController")
@WebFluxTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private WebTestClient webClient;

    private final String BOOK_ID = "1";
    private final String TEST_NAME = "TEST_NAME";

    @Test
    void shouldReturnCorrectBooksList() {
        Book firstBook = new Book();
        firstBook.setGenre(new Genre());
        List<Book> books = new ArrayList<>();
        books.add(firstBook);
        Book secondBook = new Book();
        secondBook.setGenre(new Genre());
        books.add(secondBook);
        Flux<Book> fluxBooks = Flux.fromIterable(books);
        when(bookRepository.findAll()).thenReturn(fluxBooks);
        webClient.get().uri("/books").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(BookDto.toDto(firstBook))
                .contains(BookDto.toDto(secondBook));
    }

    @Test
    void shouldReturnCorrectBookByIdInPath() {
        Book book = new Book();
        book.setGenre(new Genre());
        Mono<Book> monoBook = Mono.just(book);
        when(bookRepository.findById(BOOK_ID)).thenReturn(monoBook);
        webClient.get().uri("/books/{id}", BOOK_ID).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(BookDto.toDto(book));
    }

    @Test
    void shouldCorrectCreateBook() {
        BookDto bookDto = new BookDto();
        bookDto.setId(BOOK_ID);
        bookDto.setTitle(TEST_NAME);
        GenreDto genreDto = new GenreDto();
        genreDto.setName(TEST_NAME);
        bookDto.setGenre(genreDto);

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setGenre(GenreDto.toDomainObject(bookDto.getGenre()));
        Mono<Book> monoBook = Mono.just(book);
        when(bookRepository.save(any())).thenReturn(monoBook);
        webClient.post().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void shouldCorrectDeleteBook() {
        webClient.delete().uri("/books/{id}", BOOK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
        verify(bookRepository).deleteById(BOOK_ID);
    }
}
