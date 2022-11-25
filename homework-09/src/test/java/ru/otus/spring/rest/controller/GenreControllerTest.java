package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var genres = List.of(new Genre(1, "Genre1"), new Genre(2, "Genre2"));
        when(genreService.findAll()).thenReturn(genres);
        var expectedResult = genres.stream()
                .map(GenreDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(2)))
                .andExpect(model().attribute("genres", expectedResult));

    }

    @Test
    void shouldReturnCorrectGenreEditPageByIdInPath() throws Exception {
        Genre genre = new Genre(1, "Genre1");
        when(genreService.getGenreById(1)).thenReturn(genre);

        mvc.perform(get("/genres/edit?id={id}", genre.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", GenreDto.toDto(genre)));
    }

    @Test
    void shouldCorrectUpdateGenreName() throws Exception {
        Genre genre = new Genre(1, "Genre1");
        doNothing().when(genreService).updateGenre(genre.getId(), genre.getName());

        mvc.perform(post("/genres/edit?id={id}&name={name}", genre.getId(), genre.getName()))
                .andExpect(status().isFound());
        verify(genreService).updateGenre(genre.getId(), genre.getName());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        mvc.perform(get("/genres/create"))
                .andExpect(model().attributeExists("genre"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCorrectSaveNewGenre() throws Exception {
        Genre genre = new Genre(1, "Genre1");
        doNothing().when(genreService).createGenre(genre.getName());

        mvc.perform(post("/genres/create?name=" + genre.getName()))
                .andExpect(status().isFound());
        verify(genreService).createGenre(genre.getName());
    }

    @Test
    void shouldCorrectDeleteGenre() throws Exception {
        mvc.perform(post("/genres/delete/1"))
                .andExpect(status().isFound());
        verify(genreService, times(1)).deleteGenre(1L);
    }
}
