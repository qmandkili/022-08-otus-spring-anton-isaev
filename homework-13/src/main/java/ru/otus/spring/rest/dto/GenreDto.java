package ru.otus.spring.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private long id;
    private String name;
    private Integer accessLevel;

    public static Genre toDomainObject(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName(), dto.getAccessLevel());
    }

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName(), genre.getAccessLevel());
    }
}
