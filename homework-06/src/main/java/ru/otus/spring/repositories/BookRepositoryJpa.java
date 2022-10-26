package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Component
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Book findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> eg = em.getEntityGraph("book-author-genre-graph");
        return em.createQuery("SELECT b FROM Book b", Book.class)
                .setHint(FETCH.getKey(), eg)
                .getResultList();
    }

    @Override
    public List<Book> findByName(String name) {
        TypedQuery<Book> query = em.createQuery("SELECT b " +
                        "FROM Book b " +
                        "WHERE b.name = :name",
                Book.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id));
    }
}
