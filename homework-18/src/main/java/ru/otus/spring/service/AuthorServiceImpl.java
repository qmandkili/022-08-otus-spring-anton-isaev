package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public void createAuthor(String firstName, String secondName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setSecondName(secondName);
        authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public void updateAuthor(long id, String firstName, String secondName) {
        Author author = getAuthorById(id);
        author.setFirstName(firstName);
        author.setSecondName(secondName);
    }

    @Override
    public void deleteAuthor(long id) {
        authorRepository.deleteById(id);
    }

    private Author getAuthorByIdFallback(long id, Throwable t) {
        log.error("Fallback: return empty author. Error was: ", t.getMessage());
        return new Author(id, "N/A","N/A", null);
    }

    private Author getAuthorByIdFallback(long id) {
        log.error("Fallback: return empty author.");
        return new Author(id, "N/A","N/A", null);
    }
}