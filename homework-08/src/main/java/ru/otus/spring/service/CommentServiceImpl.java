package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final SequenceGenerator generator;

    @Transactional
    @Override
    public Comment createComment(String text) {
        Comment comment = new Comment();
        comment.setId(generator.generateSequence(Comment.SEQUENCE_NAME));
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
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
}
