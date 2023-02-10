package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @PostMapping("/api/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDto comment) {
        commentService.createBookComment(comment.getBook().getId(), comment.getText());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/comments/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") long id, @RequestBody CommentDto comment) {
        commentService.updateTextById(id, comment.getText());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<BookDto> getComments(@PathVariable("id") long id) {
        try {
            var book = BookDto.toDto(bookService.getBookById(id));
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/comments/{id}")
    public ResponseEntity<?> addComment(@PathVariable("id") long id, CommentDto comment) {
        commentService.createBookComment(id, comment.getText());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
