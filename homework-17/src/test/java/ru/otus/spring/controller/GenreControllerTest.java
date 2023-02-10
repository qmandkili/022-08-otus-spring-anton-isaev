package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.rest.controller.GenreController;
import ru.otus.spring.rest.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        var genres = List.of(new Genre(1, "Genre1"),
                new Genre(2, "Genre2"));
        when(genreService.findAll()).thenReturn(genres);
        var expectedResult = genres.stream()
                .map(GenreDto::toDto).collect(Collectors.toList());

        ResultActions resultActions = mvc.perform(get("/api/genres"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<GenreDto> returnGenres = getGenresDto(objectMapper.readValue(contentAsString, List.class));
        Assertions.assertEquals(expectedResult, returnGenres);
    }

    @Test
    void shouldReturnCorrectGenreByIdInPath() throws Exception {
        var genre = new Genre(1, "Genre1");
        when(genreService.getGenreById(1)).thenReturn(genre);

        var expectedResult = GenreDto.toDto(genre);

        MvcResult result = mvc.perform(get("/api/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        GenreDto returnGenre = objectMapper.readValue(contentAsString, GenreDto.class);
        Assertions.assertEquals(returnGenre, expectedResult);
    }

    @Test
    void shouldCorrectUpdateGenreName() throws Exception {
        var genre = new GenreDto(1, "Genre1");
        doNothing().when(genreService).updateGenre(genre.getId(), genre.getName());
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(genre);
        mvc.perform(put("/api/genres/{id}", genre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        verify(genreService).updateGenre(genre.getId(), genre.getName());
    }

    @Test
    void shouldCorrectSaveNewGenre() throws Exception {
        var genre = new GenreDto(1, "Genre1");
        doNothing().when(genreService).createGenre(genre.getName());
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(genre);

        mvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
        verify(genreService).createGenre(genre.getName());
    }

    @Test
    void shouldCorrectDeleteGenre() throws Exception {
        mvc.perform(delete("/api/genres/{id}", 1))
                .andExpect(status().isOk());
        verify(genreService).deleteGenre(1L);
    }

    private List<GenreDto> getGenresDto(List<LinkedHashMap> genres) {
        List<GenreDto> resultGenres = new ArrayList<>();
        for (LinkedHashMap g : genres) {
            GenreDto genre = new GenreDto();
            genre.setId(((Integer) g.get("id")).longValue());
            genre.setName((String) g.get("name"));
            resultGenres.add(genre);
        }
        return resultGenres;
    }
}
