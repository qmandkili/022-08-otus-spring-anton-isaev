package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        var books = bookService.getAllBooks().stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
        return books != null && !books.isEmpty()
                ? new ResponseEntity<>(books, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        try {
            var book = BookDto.toDto(bookService.getBookById(id));
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> createBook(@RequestBody BookDto book) {
        bookService.createBook(BookDto.toDomainObject(book));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") long id, @RequestBody BookDto book) {
        bookService.updateBook(id, BookDto.toDomainObject(book));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}