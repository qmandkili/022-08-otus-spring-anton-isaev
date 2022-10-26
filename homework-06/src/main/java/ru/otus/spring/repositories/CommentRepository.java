package ru.otus.spring.repositories;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment findById(long id);

    void deleteById(long id);

    void deleteByBookId(long bookId);
}
