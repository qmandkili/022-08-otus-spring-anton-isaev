package ru.otus.spring.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        var genres = genreService.findAll().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList());
        return genres != null && !genres.isEmpty()
                ? new ResponseEntity<>(genres, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable("id") long id) {
        try {
            var genre = GenreDto.toDto(genreService.getGenreById(id));
            return new ResponseEntity<>(genre, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/genres")
    public ResponseEntity<?> createGenre(@RequestBody GenreDto genre) {
        genreService.createGenre(genre.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/genres/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable("id") long id, @RequestBody GenreDto genre) {
        genreService.updateGenre(id, genre.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/genres/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}