package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.model.h2.AuthorDao;
import ru.otus.spring.domain.model.h2.BookDao;
import ru.otus.spring.domain.model.h2.CommentDao;
import ru.otus.spring.domain.model.h2.GenreDao;
import ru.otus.spring.domain.model.mongo.Author;
import ru.otus.spring.domain.model.mongo.Book;
import ru.otus.spring.domain.model.mongo.Comment;
import ru.otus.spring.domain.model.mongo.Genre;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorServiceImpl implements ProcessorService {

    private final NamedParameterJdbcOperations jdbcOperations;

    private final Map<String, Long> genreIdMap = new HashMap<>();
    private final Map<String, Long> authorIdMap = new HashMap<>();
    private final Map<String, Long> commentToBookMap = new HashMap<>();

    @Override
    public GenreDao processGenre(Genre genre) {
        log.info("Genre from mongo: " + genre.toString());
        var id = getSequence(Genre.SEQUENCE_NAME);
        genreIdMap.put(genre.getId(), id);
        return new GenreDao(id, genre.getName());
    }

    @Override
    public AuthorDao processAuthor(Author author) {
        log.info("Author from mongo: " + author.toString());
        var id = getSequence(Author.SEQUENCE_NAME);
        authorIdMap.put(author.getId(), id);
        return new AuthorDao(id, author.getFirstName(), author.getSecondName());
    }

    @Override
    public BookDao processBook(Book book) {
        log.info("Book from mongo: " + book.toString());
        var id = getSequence(Book.SEQUENCE_NAME);
        var genreId = genreIdMap.get(book.getGenre().getId());
        var authorId = authorIdMap.get(book.getAuthor().getId());
        var bookDao = new BookDao(id, book.getName(), genreId, authorId);
        for (Comment comment : book.getComments()) {
            commentToBookMap.putIfAbsent(comment.getId(), id);
        }
        return bookDao;
    }

    @Override
    public CommentDao processComment(Comment comment) {
        log.info("Comment from mongo: " + comment.toString());
        var id = getSequence(Comment.SEQUENCE_NAME);
        var bookId = commentToBookMap.get(comment.getId());
        return new CommentDao(id, comment.getText(), bookId);
    }

    private Long getSequence(String sequenceName) {
        var jdbc = jdbcOperations.getJdbcOperations();
        return jdbc.queryForObject(String.format("select next value for %s", sequenceName), Long.class);
    }
}
