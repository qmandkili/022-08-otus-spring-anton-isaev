package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public Comment createComment(String text) {
        Comment comment = new Comment();
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Transactional
    @Override
    public void updateTextById(long id, String text) {
        Comment comment = getCommentById(id);
        comment.setText(text);
    }

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void createBookComment(long bookId, String text) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));;
        Comment comment = new Comment();
        comment.setText(text);
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void addBookComment(long bookId, long commentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));;
        Comment comment = getCommentById(commentId);
        comment.setBook(book);
    }
}