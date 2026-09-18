package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;
import dev.sorokin.eventmanager.persistence.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
