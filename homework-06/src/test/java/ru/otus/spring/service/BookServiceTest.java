package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    private static final long ID = 1;
    private static final String NAME = "NAME";

    @Test
    public void createBookHappyPath() {
        Book book = new Book();
        book.setName(NAME);

        bookService.createBook(NAME);

        verify(bookRepository).save(book);
    }

    @Test
    public void getBookByIdHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        Book actualBook = bookService.getBookById(ID);

        verify(bookRepository).findById(ID);
        assertThat(actualBook).isEqualTo(book);
    }

    @Test
    public void getAllBooksHappyPath() {
        List<Book> books = List.of(mock(Book.class), mock(Book.class));
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> actualAuthors = bookRepository.findAll();

        assertThat(actualAuthors.size()).isGreaterThan(1);
        assertThat(actualAuthors).isEqualTo(books);
        verify(bookRepository).findAll();
    }

    @Test
    public void updateBookHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        book.setName(NAME);
        bookService.updateBook(ID, NAME);

        verify(bookRepository).save(book);
    }

    @Test
    public void deleteBookHappyPath() {
        bookService.deleteBook(ID);

        verify(bookRepository).deleteById(ID);
    }

    @Test
    public void addBookAuthorHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        Author author = mock(Author.class);
        when(authorRepository.findById(ID)).thenReturn(author);
        book.setAuthor(author);
        bookService.addBookAuthor(ID, ID);

        verify(bookRepository).findById(ID);
        verify(authorRepository).findById(ID);
        verify(bookRepository).save(book);
    }

    @Test
    public void addBookGenreHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        Genre genre = mock(Genre.class);
        when(genreRepository.findById(ID)).thenReturn(genre);
        book.setGenre(genre);
        bookService.addBookGenre(ID, ID);

        verify(bookRepository).findById(ID);
        verify(genreRepository).findById(ID);
        verify(bookRepository).save(book);
    }

    @Test
    public void addBookCommentHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(ID)).thenReturn(comment);
        comment.setBook(book);
        bookService.addBookComment(ID, ID);

        verify(bookRepository).findById(ID);
        verify(commentRepository).findById(ID);
        verify(commentRepository).save(comment);
    }

    @Test
    public void deleteBookAuthorHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        book.setAuthor(null);
        bookService.deleteBookAuthor(ID, ID);

        verify(bookRepository).findById(ID);
        verify(bookRepository).save(book);
    }

    @Test
    public void deleteBookGenreHappyPath() {
        Book book = mock(Book.class);
        when(bookRepository.findById(ID)).thenReturn(book);
        book.setGenre(null);
        bookService.deleteBookGenre(ID, ID);

        verify(bookRepository).findById(ID);
        verify(bookRepository).save(book);
    }

    @Test
    public void getBookCommentHappyPath() {
        Book book = mock(Book.class);
        List<Comment> comments = List.of(mock(Comment.class), mock(Comment.class));
        when(bookRepository.findById(ID)).thenReturn(book);
        when(book.getComments()).thenReturn(comments);
        bookService.getBookComments(ID);

        verify(bookRepository).findById(ID);
    }
}
