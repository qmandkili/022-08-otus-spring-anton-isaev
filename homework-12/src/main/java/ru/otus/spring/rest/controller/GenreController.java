package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("genres",
                genreService.findAll().stream()
                        .map(GenreDto::toDto)
                        .collect(Collectors.toList()));
        return "genres/list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("genre", GenreDto.toDto(genreService.getGenreById(id)));
        return "genres/edit";
    }

    @PostMapping("/edit")
    public String saveGenre(GenreDto genre) {
        genreService.updateGenre(genre.getId(), genre.getName());
        return "redirect:/genres";
    }

    @PostMapping("/delete/{id}")
    public String deleteGenre(@PathVariable long id) {
        genreService.deleteGenre(id);
        return "redirect:/genres";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("genre", new GenreDto());
        return "genres/create";
    }

    @PostMapping("/create")
    public String createGenre(GenreDto genre) {
        genreService.createGenre(genre.getName());
        return "redirect:/genres";
    }
}
