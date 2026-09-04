package dev.sorokin.eventmanager.domain.service;

import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import dev.sorokin.eventmanager.infrastructure.service.JwtService;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;
import dev.sorokin.eventmanager.persistence.entity.UserRole;
import dev.sorokin.eventmanager.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createAdmin(String login, String password, Integer age) {
        return createUser(login, password, age, UserRole.ADMIN);
    }

    public User createUser(String login, String password, Integer age) {
        return createUser(login, password, age, UserRole.USER);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDomain)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    private User createUser(String login, String password, Integer age, UserRole role) {
        UserEntity entity = new UserEntity();
        entity.setLogin(login);
        entity.setPass(passwordEncoder.encode(password));
        entity.setAge(age);
        entity.setRole(role);
        var user = userRepository.save(entity);
        return UserMapper.toDomain(user);
    }
}
