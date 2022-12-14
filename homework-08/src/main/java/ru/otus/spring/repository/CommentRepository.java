package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Comment;

public interface CommentRepository extends MongoRepository<Comment, Long> {
}
