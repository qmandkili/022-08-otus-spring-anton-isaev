package ru.otus.spring.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;
    private String name;
    private AuthorDto author;
    private GenreDto genre;
    private List<CommentDto> comments;
    private Integer accessLevel;

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getName(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()),
                book.getComments().stream()
                        .map(CommentDto::toDto)
                        .collect(Collectors.toList()),
                book.getAccessLevel());
    }

    public static BookDto toDtoWithoutComments(Book book) {
        return new BookDto(
                book.getId(),
                book.getName(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()),
                null,
                book.getAccessLevel());
    }

    public static Book toDomainObject(BookDto dto) {
        return new Book(
                dto.getId(),
                dto.getName(),
                AuthorDto.toDomainObject(dto.getAuthor()),
                GenreDto.toDomainObject(dto.getGenre()),
                null,
                dto.getAccessLevel()
        );
    }
}
