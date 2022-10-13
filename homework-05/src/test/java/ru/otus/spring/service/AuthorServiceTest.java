package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private AuthorServiceImpl authorService;

    private static final long ID = 1;
    private static final String NAME = "NAME";

    @Test
    public void createAuthorHappyPath() {
        Author author = new Author(NAME, NAME);
        authorService.createAuthor(NAME, NAME);

        verify(authorDao).insert(author);
    }

    @Test
    public void getAuthorByIdHappyPath() {
        Author author = mock(Author.class);
        when(authorDao.getById(ID)).thenReturn(author);
        Author actualAuthor = authorService.getAuthorById(ID);

        assertThat(actualAuthor).isEqualTo(author);
        verify(authorDao).getById(ID);
    }

    @Test
    public void updateAuthorHappyPath() {
        Author author = mock(Author.class);
        when(authorDao.getById(ID)).thenReturn(author);
        when(authorDao.update(author)).thenReturn(1);
        int updateResult = authorService.updateAuthor(ID, NAME, NAME);

        assertThat(updateResult).isEqualTo(1);
        verify(authorDao).update(author);
    }

    @Test
    public void deleteAuthorHappyPath() {
        authorService.deleteAuthor(ID);
        verify(authorDao).deleteById(ID);
    }
}
