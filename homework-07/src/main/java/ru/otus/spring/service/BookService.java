package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface BookService {

    void createBook(String bookName);

    Book getBookById(long id);

    List<Book> getAllBooks();

    void updateBook(long id, String name);

    void deleteBook(long id);

    void addBookAuthor(long bookId, long authorId);

    void addBookGenre(long bookId, long genreId);

    void addBookComment(long bookId, long commentId);

    void deleteBookAuthor(long bookId);

    void deleteBookGenre(long bookId);

    List<Comment> getBookComments(long id);
}