package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Comment findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id))
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id));
    }

    @Override
    public void deleteByBookId(long bookId) {
        em.createQuery("delete " +
                "from Comment c " +
                "where c.book.id = :id")
                .setParameter("id", bookId)
                .executeUpdate();
    }
}
