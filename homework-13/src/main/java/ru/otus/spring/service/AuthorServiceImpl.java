package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createAuthor(String firstName, String secondName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setSecondName(secondName);
        authorRepository.save(author);
    }

    @Override
    @PostAuthorize("hasRole('ROLE_ADMIN') and returnObject.accessLevel <= authentication.principal.accessLevel")
    public Author getAuthorById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    @PostFilter("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') " +
            "and filterObject.accessLevel <= authentication.principal.accessLevel")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateAuthor(long id, String firstName, String secondName) {
        Author author = getAuthorById(id);
        author.setFirstName(firstName);
        author.setSecondName(secondName);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAuthor(long id) {
        authorRepository.deleteById(id);
    }
}