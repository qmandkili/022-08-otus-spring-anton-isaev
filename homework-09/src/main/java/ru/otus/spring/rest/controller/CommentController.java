package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/{id}")
    public String getComments(@PathVariable long id, Model model) {
        var book = bookService.getBookById(id);
        model.addAttribute("book", BookDto.toDto(book));
        model.addAttribute("comments", book.getComments().stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList()));
        return "comments/list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("comment", CommentDto.toDto(commentService.getCommentById(id)));
        return "comments/edit";
    }

    @PostMapping("/edit")
    public String saveComment(CommentDto comment) {
        commentService.updateTextById(comment.getId(), comment.getText());
        return "redirect:/comments/" + comment.getBook().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return "redirect:/books";
    }

    @GetMapping("/create/{id}")
    public String createPage(@PathVariable("id") long id, Model model) {
        var book = bookService.getBookById(id);
        var comment = new Comment();
        comment.setBook(book);
        model.addAttribute("comment", CommentDto.toDto(comment));
        return "comments/create";
    }

    @PostMapping("/create")
    public String createComment(CommentDto comment) {
        commentService.createBookComment(comment.getBook().getId(), comment.getText());
        return "redirect:/books";
    }
}
