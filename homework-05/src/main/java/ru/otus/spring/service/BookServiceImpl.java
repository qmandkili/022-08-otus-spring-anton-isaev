package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Transactional
    public void createBook(String bookName) {
        bookDao.insert(bookName);
    }

    public Book getBookById(long id) {
        Book book = bookDao.getById(id);
        return book;
    }

    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Transactional
    public int updateBook(long id, String name) {
        Book book = bookDao.getById(id);
        book.setName(name);
        return bookDao.update(book);
    }

    @Transactional
    public void deleteBook(long id) {
        bookDao.deleteById(id);
    }

    @Transactional
    public void updateBookAuthor(long bookId, long authorId) {
        bookDao.updateBookAuthor(bookId, authorId);
    }

    @Transactional
    public void updateBookGenre(long bookId, long genreId) {
        bookDao.updateBookGenre(bookId, genreId);
    }
}
