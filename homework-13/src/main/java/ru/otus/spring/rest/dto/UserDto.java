package ru.otus.spring.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Role;
import ru.otus.spring.domain.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private Integer accessLevel;
    private List<String> roles;

    public static UserDto toDto(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return new UserDto(
                user.getUsername(),
                user.getAccessLevel(),
                user.getRoles().stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList())
        );
    }
}
