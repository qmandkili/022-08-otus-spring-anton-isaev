package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Role;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.security.AuthProvider;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    @MockBean
    private UserService userService;
    @MockBean
    private AuthProvider authProvider;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var genres = List.of(
                new Genre(1L, "Genre1", 1),
                new Genre(2L, "Genre2", 1));
        when(genreService.findAll()).thenReturn(genres);
        var expectedResult = genres.stream()
                .map(GenreDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/genres")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(2)))
                .andExpect(model().attribute("genres", expectedResult));

    }

    @Test
    void shouldReturnCorrectGenreEditPageByIdInPath() throws Exception {
        Genre genre = new Genre(1L, "Genre1", 1);
        when(genreService.getGenreById(1)).thenReturn(genre);

        mvc.perform(get("/genres/edit?id={id}", genre.getId())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", GenreDto.toDto(genre)));
    }

    @Test
    void shouldCorrectUpdateGenreName() throws Exception {
        Genre genre = new Genre(1L, "Genre1", 1);
        doNothing().when(genreService).updateGenre(genre.getId(), genre.getName());

        mvc.perform(post("/genres/edit?id={id}&name={name}", genre.getId(), genre.getName())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(genreService).updateGenre(genre.getId(), genre.getName());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        mvc.perform(get("/genres/create")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(model().attributeExists("genre"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCorrectSaveNewGenre() throws Exception {
        Genre genre = new Genre(1L, "Genre1", 1);
        doNothing().when(genreService).createGenre(genre.getName());

        mvc.perform(post("/genres/create?name=" + genre.getName())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(genreService).createGenre(genre.getName());
    }

    @Test
    void shouldCorrectDeleteGenre() throws Exception {
        mvc.perform(post("/genres/delete/1")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(genreService, times(1)).deleteGenre(1L);
    }
}
