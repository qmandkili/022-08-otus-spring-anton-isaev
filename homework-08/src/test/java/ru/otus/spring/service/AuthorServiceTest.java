package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private SequenceGenerator generator;
    @InjectMocks
    private AuthorServiceImpl authorService;

    private static final long ID = 1;
    private static final String NAME = "NAME";

    @Test
    public void createAuthorHappyPath() {
        Author author = new Author();
        author.setFirstName(NAME);
        author.setSecondName(NAME);
        author.setId(ID);
        when(generator.generateSequence(any())).thenReturn(ID);
        authorService.createAuthor(NAME, NAME);

        verify(authorRepository).save(author);
        verify(generator).generateSequence(any());
    }

    @Test
    public void getAuthorByIdHappyPath() {
        Author author = mock(Author.class);
        when(authorRepository.findById(ID)).thenReturn(Optional.of(author));
        Author actualAuthor = authorService.getAuthorById(ID);

        assertThat(actualAuthor).isEqualTo(author);
        verify(authorRepository).findById(ID);
    }

    @Test
    public void getAllAuthorsHappyPath() {
        List<Author> authors = List.of(mock(Author.class), mock(Author.class));
        when(authorRepository.findAll()).thenReturn(authors);
        List<Author> actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors.size()).isGreaterThan(1);
        assertThat(actualAuthors).isEqualTo(authors);
        verify(authorRepository).findAll();
    }

    @Test
    public void updateAuthorHappyPath() {
        Author author = mock(Author.class);
        when(authorRepository.findById(ID)).thenReturn(Optional.of(author));
        author.setFirstName(NAME);
        author.setSecondName(NAME);
        authorService.updateAuthor(ID, NAME, NAME);
    }

    @Test
    public void deleteAuthorHappyPath() {
        authorService.deleteAuthor(ID);
        verify(authorRepository).deleteById(ID);
    }
}