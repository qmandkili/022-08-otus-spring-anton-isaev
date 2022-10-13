package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {

    void createBook(String bookName);

    Book getBookById(long id);

    List<Book> getAllBooks();

    int updateBook(long id, String name);

    void deleteBook(long id);

    void updateBookAuthor(long bookId, long authorId);

    void updateBookGenre(long bookId, long genreId);
}
