package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommandsAccepter {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;

    @ShellMethod(value = "Create new book", key = {"create-book"})
    public void createBook(String name) {
        bookService.createBook(name);
    }

    @ShellMethod(value = "Get book", key = {"get-book"})
    public void getBook(Long id) {
        System.out.println(bookService.getBookById(id));
    }

    @ShellMethod(value = "Get all books", key = {"get-books"})
    public void getBook() {
        System.out.println(bookService.getAllBooks());
    }

    @ShellMethod(value = "Update book", key = {"update-book"})
    public void updateBook(long id, String name) {
        bookService.updateBook(id, name);
    }

    @ShellMethod(value = "Delete book", key = {"delete-book"})
    public void deleteBook(long id) {
        bookService.deleteBook(id);
    }

    @ShellMethod(value = "Create new author", key = {"create-author"})
    public void createAuthor(String firstName, String secondName) {
        authorService.createAuthor(firstName, secondName);
    }

    @ShellMethod(value = "Get author", key = {"get-author"})
    public void getAuthor(Long id) {
        System.out.println(authorService.getAuthorById(id));
    }

    @ShellMethod(value = "Update author", key = {"update-author"})
    public void updateAuthor(long id, String firstName, String secondName) throws Exception {
        authorService.updateAuthor(id, firstName, secondName);
    }

    @ShellMethod(value = "Delete author", key = {"delete-author"})
    public void deleteAuthor(long id) {
        authorService.deleteAuthor(id);
    }

    @ShellMethod(value = "Create new genre", key = {"create-genre"})
    public void createGenre(String name) {
        genreService.createGenre(name);
    }

    @ShellMethod(value = "Get genre", key = {"get-genre"})
    public void getGenre(Long id) {
        System.out.println(genreService.getGenreById(id));
    }

    @ShellMethod(value = "Update genre", key = {"update-genre"})
    public void updateGenre(long id, String name) {
        genreService.updateGenre(id, name);
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre"})
    public void deleteGenre(long id) {
        genreService.deleteGenre(id);
    }

    @ShellMethod(value = "Update author for book", key = {"update-book-author"})
    public void updateBookAuthor(long bookId, long authorId) {
        bookService.updateBookAuthor(bookId, authorId);
    }

    @ShellMethod(value = "Update genre for book", key = {"update-book-genre"})
    public void updateBookGenre(long bookId, long authorId) {
        bookService.updateBookGenre(bookId, authorId);
    }
}
