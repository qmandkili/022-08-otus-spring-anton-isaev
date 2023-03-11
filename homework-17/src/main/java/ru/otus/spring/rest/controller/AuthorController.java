package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        var authors = authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());
        return authors != null && !authors.isEmpty()
                ? new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") long id) {
        try {
            var author = AuthorDto.toDto(authorService.getAuthorById(id));
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/authors")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto author) {
        authorService.createAuthor(author.getFirstName(), author.getSecondName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/authors/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") long id, @RequestBody AuthorDto author) {
        authorService.updateAuthor(id, author.getFirstName(), author.getSecondName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}