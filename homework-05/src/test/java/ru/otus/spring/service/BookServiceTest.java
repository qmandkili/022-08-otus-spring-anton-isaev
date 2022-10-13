package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookDao bookDao;
    @InjectMocks
    private BookServiceImpl bookService;

    private static final long ID = 1;
    private static final String NAME = "NAME";

    @Test
    public void createBookHappyPath() {
        bookService.createBook(NAME);
        verify(bookDao).insert(NAME);
    }

    @Test
    public void getBookByIdHappyPath() {
        Book book = mock(Book.class);
        when(bookDao.getById(ID)).thenReturn(book);
        bookService.getBookById(ID);

        verify(bookDao).getById(ID);
    }

    @Test
    public void getAllBooksHappyPath() {
        Book book = mock(Book.class);
        List allBooks = List.of(book);
        when(bookDao.getAll()).thenReturn(allBooks);
        List actualBooks = bookService.getAllBooks();

        assertThat(actualBooks).isEqualTo(allBooks);
        verify(bookDao).getAll();
    }

    @Test
    public void updateBookHappyPath() {
        Book book = mock(Book.class);
        when(bookDao.getById(ID)).thenReturn(book);
        when(bookDao.update(book)).thenReturn(1);
        int updateResult = bookService.updateBook(ID, NAME);

        assertThat(updateResult).isEqualTo(1);
        verify(bookDao).update(book);
    }

    @Test
    public void deleteBookHappyPath() {
        bookService.deleteBook(ID);
        verify(bookDao).deleteById(ID);
    }

    @Test
    public void updateBookAuthorHappyPath() {
        bookService.updateBookAuthor(ID, ID);
        verify(bookDao).updateBookAuthor(ID, ID);
    }

    @Test
    public void updateBookGenreHappyPath() {
        bookService.updateBookGenre(ID, ID);
        verify(bookDao).updateBookGenre(ID, ID);
    }
}
