package ru.otus.spring.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private String name;

    public static Genre toDomainObject(GenreDto dto) {
        return new Genre(dto.getName());
    }

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getName());
    }
}
