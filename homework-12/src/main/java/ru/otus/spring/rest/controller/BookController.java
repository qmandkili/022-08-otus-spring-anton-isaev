package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks().stream()
                .map(BookDto::toDtoWithoutComments)
                .collect(Collectors.toList()));
        return "books/list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", BookDto.toDtoWithoutComments(bookService.getBookById(id)));
        model.addAttribute("authors", authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("genres", genreService.findAll().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList()));
        return "books/edit";
    }

    @PostMapping("/edit")
    public String editBook(BookDto book) {
        bookService.updateBook(book.getId(), book.getName());
        bookService.addBookAuthor(book.getId(), book.getAuthor().getId());
        bookService.addBookGenre(book.getId(), book.getGenre().getId());
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("genres", genreService.findAll().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList()));
        return "books/create";
    }

    @PostMapping("/create")
    public String createBook(BookDto bookDto) {
        var book = BookDto.toDomainObject(bookDto);
        book.setAuthor(authorService.getAuthorById(bookDto.getAuthor().getId()));
        book.setGenre(genreService.getGenreById(bookDto.getGenre().getId()));
        bookService.createBook(book);
        return "redirect:/books";
    }
}
