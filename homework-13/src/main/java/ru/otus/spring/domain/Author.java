package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
@NamedEntityGraph(name = "book-author-graph",attributeNodes = {@NamedAttributeNode("books")})
public class Author implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    @Column(name = "access_level", nullable = false)
    private Integer accessLevel = 1;

    @Override
    public String toString() {
        return id + " " + firstName + " " + secondName + ". Количество книг: " + books.size();
    }
}