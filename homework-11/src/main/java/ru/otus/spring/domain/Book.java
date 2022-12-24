package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class Book {

    @Id
    private String id;
    private String title;
    private Genre genre;

    @Override
    public String toString() {
        return "Book{id = " + id
                + ", title = " + title + ", genre = " + genre.getName() + "}";
    }
}
