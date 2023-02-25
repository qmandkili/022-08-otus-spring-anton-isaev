package ru.otus.spring.rest.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    private final static String COMMENT_SERVICE = "commentSerivce";

    @GetMapping("/{id}")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadGetCommentsByBookIdListPage",
            type = Bulkhead.Type.SEMAPHORE)
    public String getComments(@PathVariable long id, Model model) {
        var book = bookService.getBookById(id);
        model.addAttribute("book", BookDto.toDto(book));
        model.addAttribute("comments", book.getComments().stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList()));
        return "comments/list";
    }

    @GetMapping("/edit")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadGetCommentEditPageById",
            type = Bulkhead.Type.SEMAPHORE)
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("comment", CommentDto.toDto(commentService.getCommentById(id)));
        return "comments/edit";
    }

    @PostMapping("/edit")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadUpdateComment", type = Bulkhead.Type.SEMAPHORE)
    public String saveComment(CommentDto comment) {
        commentService.updateTextById(comment.getId(), comment.getText());
        return "redirect:/comments/" + comment.getBook().getId();
    }

    @PostMapping("/delete/{id}")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadDeleteCommentById", type = Bulkhead.Type.SEMAPHORE)
    public String deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return "redirect:/books";
    }

    @GetMapping("/create/{id}")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadCreateCommentPage", type = Bulkhead.Type.SEMAPHORE)
    public String createPage(@PathVariable("id") long id, Model model) {
        var book = bookService.getBookById(id);
        var comment = new Comment();
        comment.setBook(book);
        model.addAttribute("comment", CommentDto.toDto(comment));
        return "comments/create";
    }

    @PostMapping("/create")
    @Bulkhead(name = COMMENT_SERVICE, fallbackMethod = "bulkheadCreateComment", type = Bulkhead.Type.SEMAPHORE)
    public String createComment(CommentDto comment) {
        commentService.createBookComment(comment.getBook().getId(), comment.getText());
        return "redirect:/books";
    }

    public String bulkheadGetCommentEditPageById(long id, Model model, Throwable t) {
        log.info("bulkheadGetCommentEditPageById. Error was: ", t.getMessage());
        var author = new AuthorDto(0, "N/A", "N/A");
        var genre = new GenreDto(0, "N/A");
        var book = new BookDto(0, "N/A", author, genre, List.of());
        var comment = new CommentDto(id, "N/A", book);
        model.addAttribute("book", book);
        model.addAttribute("comments", List.of(comment));
        return "comments/edit";
    }

    public String bulkheadGetCommentsByBookIdListPage(long id, Model model, Throwable t) {
        log.info("bulkheadGetCommentsByBookIdListPage. Error was: ", t.getMessage());
        var author = new AuthorDto(0, "N/A", "N/A");
        var genre = new GenreDto(0, "N/A");
        var book = new BookDto(0, "N/A", author, genre, List.of());
        model.addAttribute("book", book);
        model.addAttribute("comments", List.of(new CommentDto(id, "N/A", book)));
        return "comments/list";
    }

    public String bulkheadUpdateComment(CommentDto comment, Throwable t) {
        log.info("bulkheadUpdateComment. Error was: ", t.getMessage());
        return "redirect:/comments/" + comment.getBook().getId();
    }

    public String bulkheadDeleteCommentById(long id, Throwable t) {
        log.info("bulkheadDeleteCommentById. Error was: ", t.getMessage());
        return "redirect:/books";
    }

    public String bulkheadCreateCommentPage(long id, Model model, Throwable t) {
        log.info("bulkheadCreateCommentPage. Error was: ", t.getMessage());
        return "redirect:/comments/" + id;
    }

    public String bulkheadCreateComment(CommentDto comment, Throwable t) {
        log.info("bulkheadCreateComment. Error was: ", t.getMessage());
        return "redirect:/books";
    }
}
