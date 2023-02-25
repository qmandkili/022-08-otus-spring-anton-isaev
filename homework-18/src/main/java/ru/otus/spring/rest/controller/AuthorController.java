package ru.otus.spring.rest.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/authors")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    private final static String AUTHOR_SERVICE = "authorService";

    @GetMapping
    @Bulkhead(name = AUTHOR_SERVICE, fallbackMethod = "bulkheadGetAuthorListPage", type = Bulkhead.Type.SEMAPHORE)
    public String listPage(Model model) {
        model.addAttribute("authors", authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
        return "authors/list";
    }

    @GetMapping("/edit")
    @Bulkhead(name = AUTHOR_SERVICE, fallbackMethod = "bulkheadGetAuthorEditPageById", type = Bulkhead.Type.SEMAPHORE)
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("author", AuthorDto.toDto(authorService.getAuthorById(id)));
        return "authors/edit";
    }

    @PostMapping("/edit")
    public String saveAuthor(AuthorDto author) {
        authorService.updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());
        return "redirect:/authors";
    }

    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("author", new AuthorDto());
        return "authors/create";
    }

    @PostMapping("/create")
    public String createAuthor(AuthorDto author) {
        authorService.createAuthor(author.getFirstName(), author.getSecondName());
        return "redirect:/authors";
    }

    public String bulkheadGetAuthorEditPageById(long id, Model model, Throwable t) {
        log.info("bulkheadGetAuthorById. Error was: ", t.getMessage());
        model.addAttribute("author", new AuthorDto(id, "N/A", "N/A"));
        return "authors/edit";
    }
}
