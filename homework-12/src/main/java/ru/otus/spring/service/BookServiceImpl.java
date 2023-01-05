package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Override
    public void createBook(String bookName) {
        Book book = new Book();
        book.setName(bookName);
        bookRepository.save(book);
    }

    @Override
    public void createBook(Book book) {
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
    }

    @Transactional
    @Override
    public void deleteBook(long id) {
        commentRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void addBookAuthor(long bookId, long authorId) {
        Book book = getBookById(bookId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        book.setAuthor(author);
    }

    @Transactional
    @Override
    public void addBookGenre(long bookId, long genreId) {
        Book book = getBookById(bookId);
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        book.setGenre(genre);
    }

    @Transactional
    @Override
    public void addBookComment(long bookId, long commentId) {
        Book book = getBookById(bookId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setBook(book);
    }

    @Transactional
    @Override
    public void deleteBookAuthor(long bookId) {
        Book book = getBookById(bookId);
        book.setAuthor(null);
    }

    @Transactional
    @Override
    public void deleteBookGenre(long bookId) {
        Book book = getBookById(bookId);
        book.setGenre(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getBookComments(long id) {
        Book book = getBookById(id);
        return book.getComments().stream().collect(Collectors.toList());
    }
}