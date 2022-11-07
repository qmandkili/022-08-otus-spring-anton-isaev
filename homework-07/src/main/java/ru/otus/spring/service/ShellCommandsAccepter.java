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
    private final CommentService commentService;

    @ShellMethod(value = "Create new book", key = {"create-book"})
    public void createBook(String name) {
        bookService.createBook(name);
    }

    @ShellMethod(value = "Get book", key = {"get-book"})
    public void getBook(Long id) {
        System.out.println(bookService.getBookById(id));
    }

    @ShellMethod(value = "Get all books", key = {"get-books"})
    public void getAllBooks() {
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

    @ShellMethod(value = "Get all authors", key = {"get-authors"})
    public void getAllAuthors() {
        System.out.println(authorService.findAll());
    }

    @ShellMethod(value = "Update author", key = {"update-author"})
    public void updateAuthor(long id, String firstName, String secondName) {
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

    @ShellMethod(value = "Get all genres", key = {"get-genres"})
    public void getAllGenres() {
        System.out.println(genreService.findAll());
    }

    @ShellMethod(value = "Update genre", key = {"update-genre"})
    public void updateGenre(long id, String name) {
        genreService.updateGenre(id, name);
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre"})
    public void deleteGenre(long id) {
        genreService.deleteGenre(id);
    }

    @ShellMethod(value = "Create new comment", key = {"create-comment"})
    public void createComment(String commentValue) {
        commentService.createComment(commentValue);
    }

    @ShellMethod(value = "Get comment", key = {"get-comment"})
    public void getComment(Long id) {
        System.out.println(commentService.getCommentById(id));
    }

    @ShellMethod(value = "Update comment", key = {"update-comment"})
    public void updateComment(long id, String commentValue) {
        commentService.updateTextById(id, commentValue);
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment"})
    public void deleteComment(long id) {
        commentService.deleteComment(id);
    }

    @ShellMethod(value = "Get book comments", key = {"get-book-comment"})
    public void getBookComments(long id) {
        System.out.println(bookService.getBookComments(id));
    }

    @ShellMethod(value = "Add author to book", key = {"add-book-author"})
    public void addBookAuthor(long bookId, long authorId) {
        bookService.addBookAuthor(bookId, authorId);
    }

    @ShellMethod(value = "Add genre to book", key = {"add-book-genre"})
    public void addBookGenre(long bookId, long authorId) {
        bookService.addBookGenre(bookId, authorId);
    }

    @ShellMethod(value = "Add comment to book", key = {"add-book-comment"})
    public void addBookComment(long bookId, long commentId) {
        commentService.addBookComment(bookId, commentId);
    }

    @ShellMethod(value = "Create comment to book", key = {"create-book-comment"})
    public void createBookComment(long bookId, String text) {
        commentService.createBookComment(bookId, text);
    }

    @ShellMethod(value = "Delete author from book", key = {"delete-book-author"})
    public void deleteBookAuthor(long bookId) {
        bookService.deleteBookAuthor(bookId);
    }

    @ShellMethod(value = "Delete genre from book", key = {"delete-book-genre"})
    public void deleteBookGenre(long bookId) {
        bookService.deleteBookGenre(bookId);
    }
}
