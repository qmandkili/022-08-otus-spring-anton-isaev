package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Component
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Author findById(long id) {
        EntityGraph<?> eg = em.getEntityGraph("book-author-graph");
        return Optional.ofNullable(
                em.createQuery("select a from Author a where a.id = :id", Author.class)
                        .setParameter("id", id)
                        .setHint(FETCH.getKey(), eg)
                        .getSingleResult())
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    public List<Author> findAll() {
        EntityGraph<?> eg = em.getEntityGraph("book-author-graph");
        return em.createQuery("select a from Author a", Author.class)
                .setHint(FETCH.getKey(), eg)
                .getResultList();
    }

    @Override
    public List<Author> findByName(String firstName, String secondName) {
        return em.createQuery("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.secondName = :secondName",
                        Author.class)
                .setParameter("firstName", firstName)
                .setParameter("secondName", secondName)
                .getResultList();
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id));
    }
}
