package ru.otus.spring.rest.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.rest.dto.BookDto;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    private final static String BOOK_SERVICE = "bookService";

    @GetMapping
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadGetBookListPage", type = Bulkhead.Type.SEMAPHORE)
    public String listPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks().stream()
                .map(BookDto::toDtoWithoutComments)
                .collect(Collectors.toList()));
        return "books/list";
    }

    @GetMapping("/edit")
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadGetBookEditPageById", type = Bulkhead.Type.SEMAPHORE)
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
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadEditBook", type = Bulkhead.Type.SEMAPHORE)
    public String editBook(BookDto book) {
        bookService.updateBook(book.getId(), book.getName());
        bookService.addBookAuthor(book.getId(), book.getAuthor().getId());
        bookService.addBookGenre(book.getId(), book.getGenre().getId());
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadDeleteBookById", type = Bulkhead.Type.SEMAPHORE)
    public String deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/create")
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadCreateBookPage", type = Bulkhead.Type.SEMAPHORE)
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
    @Bulkhead(name = BOOK_SERVICE, fallbackMethod = "bulkheadCreateBook", type = Bulkhead.Type.SEMAPHORE)
    public String createBook(BookDto bookDto) {
        var book = BookDto.toDomainObject(bookDto);
        book.setAuthor(authorService.getAuthorById(bookDto.getAuthor().getId()));
        book.setGenre(genreService.getGenreById(bookDto.getGenre().getId()));
        bookService.createBook(book);
        return "redirect:/books";
    }

    public String bulkheadGetBookListPage(Model model, Throwable t) {
        log.info("bulkheadGetBookListPage. Error was: ", t.getMessage());
        var author = new AuthorDto(0, "N/A", "N/A");
        var genre = new GenreDto(0, "N/A");
        model.addAttribute("books", List.of(new BookDto(0, "N/A", author, genre, List.of())));
        return "books/list";
    }

    public String bulkheadGetBookEditPageById(long id, Model model, Throwable t) {
        log.info("bulkheadGetBookEditPageById. Error was: ", t.getMessage());
        var author = new AuthorDto(0, "N/A", "N/A");
        var genre = new GenreDto(0, "N/A");
        model.addAttribute("book", List.of(new BookDto(id, "N/A", author, genre, List.of())));
        model.addAttribute("genres", List.of(genre));
        model.addAttribute("authors", List.of(author));
        return "books/edit";
    }

    public String bulkheadEditBook(BookDto book, Throwable t) {
        log.info("bulkheadEditBook. Error was: ", t.getMessage());
        return "redirect:/books";
    }

    public String bulkheadDeleteBookById(long id, Throwable t) {
        log.info("bulkheadDeleteBookById. Error was: ", t.getMessage());
        return "redirect:/books";
    }

    public String bulkheadCreateBookPage(Model model, Throwable t) {
        log.info("bulkheadCreateBookPage. Error was: ", t.getMessage());
        model.addAttribute("book", new BookDto());
        var author = new AuthorDto(0, "N/A", "N/A");
        var genre = new GenreDto(0, "N/A");
        model.addAttribute("genres", List.of(genre));
        model.addAttribute("authors", List.of(author));
        return "books/create";
    }

    public String bulkheadCreateBook(BookDto book, Throwable t) {
        log.info("bulkheadCreateBook. Error was: ", t.getMessage());
        return "redirect:/books";
    }
}
