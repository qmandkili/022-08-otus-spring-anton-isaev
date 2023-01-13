package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("authors", authorService.findAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
        return "authors/list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("author", AuthorDto.toDto(authorService.getAuthorById(id)));
        return "authors/edit";
    }

    @PostMapping("/edit")
    public String saveAuthor(AuthorDto author) {
        authorService.updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());
        return "redirect:/authors";
    }

    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable int id) {
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
}
