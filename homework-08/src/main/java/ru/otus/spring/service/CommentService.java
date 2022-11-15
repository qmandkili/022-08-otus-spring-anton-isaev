package ru.otus.spring.service;

import ru.otus.spring.domain.Comment;

public interface CommentService {

    Comment createComment(String text);

    Comment getCommentById(long id);

    void updateTextById(long id, String text);

    void deleteComment(long id);
}
