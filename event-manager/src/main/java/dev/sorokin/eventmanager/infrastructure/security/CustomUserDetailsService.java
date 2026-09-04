package dev.sorokin.eventmanager.infrastructure.security;

import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;
import dev.sorokin.eventmanager.persistence.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("User not found by login: " + username));

        return new User(
            userEntity.getLogin(),
            userEntity.getPass(),
            List.of(new SimpleGrantedAuthority(userEntity.getRole().toString()))
        );
    }
}
