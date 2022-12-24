package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Book;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;

@RestController
@CrossOrigin
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/books")
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(BookDto::toDto);
    }

    @GetMapping("/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(BookDto::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/books")
    public Mono<ResponseEntity<Book>> createBook(@RequestBody BookDto book) {
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setGenre(GenreDto.toDomainObject(book.getGenre()));
        return bookRepository.save(newBook)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }
}
