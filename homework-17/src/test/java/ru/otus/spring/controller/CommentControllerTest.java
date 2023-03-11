package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.rest.controller.CommentController;
import ru.otus.spring.rest.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;
    @MockBean
    private BookService bookService;

    @Test
    void shouldCorrectUpdateCommentName() throws Exception {
        var comment = new CommentDto(1, "Comment1", null);
        doNothing().when(commentService).updateTextById(comment.getId(), comment.getText());
        String requestJson = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(comment);
        mvc.perform(put("/api/comments/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
        verify(commentService).updateTextById(comment.getId(), comment.getText());
    }

    @Test
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(delete("/api/comments/{id}", 1))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(1L);
    }
}
