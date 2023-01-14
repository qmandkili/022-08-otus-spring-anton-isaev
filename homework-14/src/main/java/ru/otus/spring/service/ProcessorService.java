package ru.otus.spring.service;

import ru.otus.spring.domain.model.h2.AuthorDao;
import ru.otus.spring.domain.model.h2.BookDao;
import ru.otus.spring.domain.model.h2.CommentDao;
import ru.otus.spring.domain.model.h2.GenreDao;
import ru.otus.spring.domain.model.mongo.Author;
import ru.otus.spring.domain.model.mongo.Book;
import ru.otus.spring.domain.model.mongo.Comment;
import ru.otus.spring.domain.model.mongo.Genre;

public interface ProcessorService {
    
    GenreDao processGenre(Genre genre);

    AuthorDao processAuthor(Author author);

    BookDao processBook(Book book);

    CommentDao processComment(Comment comment);
}
