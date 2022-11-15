package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final SequenceGenerator generator;

    @Transactional
    @Override
    public void createBook(String bookName) {
        Book book = new Book();
        book.setId(generator.generateSequence(Book.SEQUENCE_NAME));
        book.setName(bookName);
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void updateBook(long id, String name) {
        Book book = getBookById(id);
        book.setName(name);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void addBookAuthor(long bookId, long authorId) {
        Book book = getBookById(bookId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));;
        book.setAuthor(author);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void addBookGenre(long bookId, long genreId) {
        Book book = getBookById(bookId);
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found"));;
        book.setGenre(genre);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void addBookComment(long bookId, long commentId) {
        Book book = getBookById(bookId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        book.getComments().add(comment);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBookAuthor(long bookId) {
        Book book = getBookById(bookId);
        book.setAuthor(null);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBookGenre(long bookId) {
        Book book = getBookById(bookId);
        book.setGenre(null);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public List<Comment> getBookComments(long id) {
        Book book = getBookById(id);
        return book.getComments().stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void createBookComment(long bookId, String text) {
        Book book = getBookById(bookId);
        Comment comment = new Comment();
        comment.setId(generator.generateSequence(Comment.SEQUENCE_NAME));
        comment.setText(text);
        book.getComments().add(comment);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteCommentsByBookId(long id) {
        Book book = getBookById(id);
        commentRepository.deleteAll(book.getComments());
        book.setComments(null);
        bookRepository.save(book);
    }
}
