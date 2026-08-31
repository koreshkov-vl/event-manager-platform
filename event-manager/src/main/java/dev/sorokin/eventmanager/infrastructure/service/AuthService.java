package dev.sorokin.eventmanager.infrastructure.service;

import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;
import dev.sorokin.eventmanager.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String authUser(String login, String password) {
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Invalid login or password"));

        if (!passwordEncoder.matches(password, userEntity.getPass())) {
            throw new UserNotFoundException("Invalid login or password");
        }

        User user = UserMapper.toDomain(userEntity);
        return jwtService.generateToken(user);
    }
}
