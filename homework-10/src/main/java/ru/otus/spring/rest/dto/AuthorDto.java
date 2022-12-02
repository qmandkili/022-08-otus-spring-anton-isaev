package ru.otus.spring.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private long id;
    private String firstName;
    private String secondName;

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFirstName(), author.getSecondName());
    }

    public static Author toDomainObject(AuthorDto dto) {
        return new Author(dto.getId(), dto.getFirstName(), dto.getSecondName(), null);
    }
}
