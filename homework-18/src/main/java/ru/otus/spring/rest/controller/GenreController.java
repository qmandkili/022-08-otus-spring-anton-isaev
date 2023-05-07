package ru.otus.spring.rest.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final GenreService genreService;

    private final static String GENRE_SERVICE = "genreService";

    @GetMapping
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadGetGenreListPage", type = Bulkhead.Type.SEMAPHORE)
    public String listPage(Model model) {
        model.addAttribute("genres",
                genreService.findAll().stream()
                        .map(GenreDto::toDto)
                        .collect(Collectors.toList()));
        return "genres/list";
    }

    @GetMapping("/edit")
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadGetGenreEditPageById", type = Bulkhead.Type.SEMAPHORE)
    public String editPage(@RequestParam("id") long id, Model model) {
        model.addAttribute("genre", GenreDto.toDto(genreService.getGenreById(id)));
        return "genres/edit";
    }

    @PostMapping("/edit")
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadUpdateGenre", type = Bulkhead.Type.SEMAPHORE)
    public String saveGenre(GenreDto genre) {
        genreService.updateGenre(genre.getId(), genre.getName());
        return "redirect:/genres";
    }

    @PostMapping("/delete/{id}")
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadDeleteGenreById", type = Bulkhead.Type.SEMAPHORE)
    public String deleteGenre(@PathVariable long id) {
        genreService.deleteGenre(id);
        return "redirect:/genres";
    }

    @GetMapping("/create")
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadCreateGenrePage", type = Bulkhead.Type.SEMAPHORE)
    public String createPage(Model model) {
        model.addAttribute("genre", new GenreDto());
        return "genres/create";
    }

    @PostMapping("/create")
    @Bulkhead(name = GENRE_SERVICE, fallbackMethod = "bulkheadCreateGenre", type = Bulkhead.Type.SEMAPHORE)
    public String createGenre(GenreDto genre) {
        genreService.createGenre(genre.getName());
        return "redirect:/genres";
    }

    public String bulkheadGetGenreListPage(Model model, Throwable t) {
        log.info("bulkheadGetGenreListPage. Error was: ", t.getMessage());
        model.addAttribute("genres", List.of(new GenreDto(0, "N/A")));
        return "genres/list";
    }

    public String bulkheadGetGenreEditPageById(long id, Model model, Throwable t) {
        log.info("bulkheadGetGenreEditPageById. Error was: ", t.getMessage());
        model.addAttribute("genre", new GenreDto(id, "N/A"));
        return "genres/edit";
    }

    public String bulkheadUpdateGenre(GenreDto genre, Throwable t) {
        log.info("bulkheadUpdateGenre. Error was: ", t.getMessage());
        return "redirect:/genres";
    }

    public String bulkheadDeleteGenreById(long id, Throwable t) {
        log.info("bulkheadDeleteGenreById. Error was: ", t.getMessage());
        return "redirect:/genres";
    }

    public String bulkheadCreateGenrePage(Model model, Throwable t) {
        log.info("bulkheadCreateGenrePage. Error was: ", t.getMessage());
        model.addAttribute("genre", new GenreDto(0, "N/A"));
        return "genres/create";
    }

    public String bulkheadCreateGenre(GenreDto genre, Throwable t) {
        log.info("bulkheadCreateGenre. Error was: ", t.getMessage());
        return "redirect:/genres";
    }
}
