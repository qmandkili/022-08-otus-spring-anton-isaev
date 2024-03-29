package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name="book-author-genre-graph", attributeNodes={
        @NamedAttributeNode("author"),
        @NamedAttributeNode("genre")
})
public class Book implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToMany(targetEntity = Comment.class)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    @Column(name = "access_level", nullable = false)
    private Integer accessLevel = 1;

    public String toString() {
        String authorString = author != null ? String.format(". Автор: %s %s", author.getFirstName(), author.getSecondName()) : "";
        String genreString = genre != null ? String.format(". Жанр: %s", genre.getName()) : "";
        return id +
                " Название: " + name +
                authorString +
                genreString;
    }
}