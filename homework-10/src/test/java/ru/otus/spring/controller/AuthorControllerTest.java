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
import ru.otus.spring.domain.Author;
import ru.otus.spring.rest.controller.AuthorController;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        var authors = List.of(new Author(1, "Author1", "Author1", null),
                new Author(2, "Author2", "Author2", null));
        when(authorService.findAll()).thenReturn(authors);
        var expectedResult = authors.stream()
                .map(AuthorDto::toDto).collect(Collectors.toList());

        ResultActions resultActions = mvc.perform(get("/api/authors"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<AuthorDto> returnAuthors = getAuthorsDto(objectMapper.readValue(contentAsString, List.class));
        Assertions.assertEquals(expectedResult, returnAuthors);
    }

    @Test
    void shouldReturnCorrectAuthorByIdInPath() throws Exception {
        var author = new Author(1, "Author1", "Author1", null);
        when(authorService.getAuthorById(1)).thenReturn(author);

        var expectedResult = AuthorDto.toDto(author);

        MvcResult result = mvc.perform(get("/api/authors/{id}", author.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        AuthorDto returnAuthor = objectMapper.readValue(contentAsString, AuthorDto.class);
        Assertions.assertEquals(returnAuthor, expectedResult);
    }

    @Test
    void shouldCorrectUpdateAuthorName() throws Exception {
        var author = new AuthorDto(1, "Author1", "Author1");
        doNothing().when(authorService).updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(author);
        mvc.perform(put("/api/authors/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        verify(authorService).updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());
    }

    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        var author = new AuthorDto(1, "Author1", "Author1");
        doNothing().when(authorService).createAuthor(author.getFirstName(), author.getSecondName());
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(author);

        mvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
        verify(authorService).createAuthor(author.getFirstName(), author.getSecondName());
    }

    @Test
    void shouldCorrectDeleteAuthor() throws Exception {
        mvc.perform(delete("/api/authors/{id}", 1))
                .andExpect(status().isOk());
        verify(authorService).deleteAuthor(1L);
    }

    private List<AuthorDto> getAuthorsDto(List<LinkedHashMap> authors) {
        List<AuthorDto> resultAuthors = new ArrayList<>();
        for (LinkedHashMap a : authors) {
            AuthorDto author = new AuthorDto();
            author.setId(((Integer) a.get("id")).longValue());
            author.setFirstName((String) a.get("firstName"));
            author.setSecondName((String) a.get("secondName"));
            resultAuthors.add(author);
        }
        return resultAuthors;
    }
}
