package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Transactional
    public void createAuthor(String firstName, String secondName) {
        Author author = new Author(firstName, secondName);
        authorDao.insert(author);
    }

    public Author getAuthorById(long id) {
        return authorDao.getById(id);
    }

    @Transactional
    public int updateAuthor(long id, String firstName, String secondName) {
        Author author = authorDao.getById(id);
        author.setFirstName(firstName);
        author.setSecondName(secondName);
        return authorDao.update(author);
    }

    @Transactional
    public void deleteAuthor(long id) {
        authorDao.deleteById(id);
    }
}
