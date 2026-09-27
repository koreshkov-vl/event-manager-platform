package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import dev.sorokin.eventmanager.infrastructure.security.AuthUser;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;
import dev.sorokin.eventmanager.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class GetterUserService {

    private final UserRepository userRepository;

    public GetterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserEntity getUserFromContext() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + login));
    }

    public Optional<Long> getUserIdFromContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthUser user)) {
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }
}
