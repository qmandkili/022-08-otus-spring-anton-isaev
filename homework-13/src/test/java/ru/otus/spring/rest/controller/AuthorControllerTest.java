package ru.otus.spring.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Role;
import ru.otus.spring.rest.dto.AuthorDto;
import ru.otus.spring.security.AuthProvider;
import ru.otus.spring.service.AuthorService;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthProvider authProvider;

    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        var authors = List.of(
                new Author(1L, "Author1", "Author1", null, 1),
                new Author(2L, "Author2", "Author2", null, 1));
        when(authorService.findAll()).thenReturn(authors);
        var expectedResult = authors.stream()
                .map(AuthorDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/authors")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", hasSize(2)))
                .andExpect(model().attribute("authors", expectedResult));
    }

    @Test
    void shouldReturnCorrectAuthorEditPageByIdInPath() throws Exception {
        var author = new Author(1L, "Author1", "Author1", null, 1);
        when(authorService.getAuthorById(1)).thenReturn(author);

        var expectedResult = AuthorDto.toDto(author);

        mvc.perform(get("/authors/edit?id={id}", author.getId())
                .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("author", expectedResult));
    }

    @Test
    void shouldCorrectUpdateAuthorName() throws Exception {
        var author = new AuthorDto(1L, "Author1", "Author1", 1);
        doNothing().when(authorService).updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());

        mvc.perform(post("/authors/edit?id={id}&firstName={name}&secondName={secondName}",
                        author.getId(), author.getFirstName(), author.getSecondName())
                .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(authorService).updateAuthor(author.getId(), author.getFirstName(), author.getSecondName());
    }

    @Test
    void shouldReturnCorrectCreatePage() throws Exception {
        mvc.perform(get("/authors/create")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(model().attributeExists("author"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        var author = new AuthorDto(1L, "Author1", "Author1", 1);
        doNothing().when(authorService).createAuthor(author.getFirstName(), author.getSecondName());

        mvc.perform(post("/authors/create?firstName={firstName}&secondName={secondName}",
                        author.getFirstName(), author.getSecondName())
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(authorService).createAuthor(author.getFirstName(), author.getSecondName());
    }

    @Test
    void shouldCorrectDeleteAuthor() throws Exception {
        mvc.perform(post("/authors/delete/1")
                        .with(user("admin").password("password").authorities(Role.ADMIN)))
                .andExpect(status().isFound());
        verify(authorService, times(1)).deleteAuthor(1);
    }
}
