package ru.otus.spring.domain;

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
    private long id;
    private String name;
    @DBRef
    private Author author;
    @DBRef
    private Genre genre;
    @DBRef
    private List<Comment> comments;

    @Override
    public String toString() {
        String authorString = author != null ?
                String.format(", author = %s %s", author.getFirstName(), author.getSecondName()) : "";
        String genreString = genre != null ?
                String.format(", genre = %s", genre.getName()) : "";
        String commentsString = comments != null &&
                comments.size() != 0 ? String.format(", comments = %s", comments) : "";
        return "Book{" +
                "id = " + id +
                ", name = " + name +
                authorString +
                genreString +
                commentsString +
                "}";
    }
}
