package ru.otus.spring.domain.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class Book {

    @Transient
    public static final String SEQUENCE_NAME ="books_sequence";

    @Id
    private String id;
    private String name;
    @DBRef
    private Author author;
    @DBRef
    private Genre genre;
    @DBRef
    private List<Comment> comments;

    public Book(String name, Author author, Genre genre, List<Comment> comments) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

    @Override
    public String toString() {
        String authorString = author != null ?
                String.format(". Автор: %s %s", author.getFirstName(), author.getSecondName()) : "";
        String genreString = genre != null ? String.format(". Жанр: %s", genre.getName()) : "";
        return id +
                " Название: " + name +
                authorString +
                genreString;
    }
}
