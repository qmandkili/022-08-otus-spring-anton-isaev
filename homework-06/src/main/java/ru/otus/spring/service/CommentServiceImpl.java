package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Comment createComment(String text) {
        Comment comment = new Comment();
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateTextById(long id, String text) {
        Comment comment = getCommentById(id);
        comment.setText(text);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void createBookComment(long bookId, String text) {
        Book book = bookRepository.findById(bookId);
        Comment comment = new Comment();
        comment.setText(text);
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void addBookComment(long bookId, long commentId) {
        Book book = bookRepository.findById(bookId);
        Comment comment = commentRepository.findById(commentId);
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteByBookId(long id) {
        commentRepository.deleteByBookId(id);
    }
}
