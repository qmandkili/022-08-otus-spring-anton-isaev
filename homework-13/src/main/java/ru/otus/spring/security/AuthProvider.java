package ru.otus.spring.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Role;
import ru.otus.spring.domain.User;
import ru.otus.spring.repositories.UserRepository;
import ru.otus.spring.rest.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthProvider(
            UserRepository userRepository,
            @Lazy PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByUsername(username);

        if (Objects.nonNull(user) && passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
            }
            return new UsernamePasswordAuthenticationToken
                    (UserDto.toDto(user), password, authorities);
        } else {
            throw new BadCredentialsException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
