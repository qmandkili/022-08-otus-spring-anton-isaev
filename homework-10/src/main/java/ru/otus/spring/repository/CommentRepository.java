package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "delete from Comments c where c.book_id = :book_id", nativeQuery = true)
    void deleteByBookId(@Param("book_id") long id);
}
