package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookDao {

    long insert(Book book);

    long insert(String bookName);

    Book getById(long id);

    Book getByName(String name);

    List<Book> getAll();

    void deleteById(long id);

    int update(Book book);

    int updateBookAuthor(long bookId, long authorId);

    int updateBookGenre(long bookId, long genreId);
}
