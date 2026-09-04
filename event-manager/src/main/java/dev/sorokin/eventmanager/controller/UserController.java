package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.controller.dto.AuthDto;
import dev.sorokin.eventmanager.controller.dto.JwtTokenDto;
import dev.sorokin.eventmanager.controller.dto.RegisterDto;
import dev.sorokin.eventmanager.controller.dto.UserDto;
import dev.sorokin.eventmanager.domain.service.UserService;
import dev.sorokin.eventmanager.infrastructure.service.AuthService;
import dev.sorokin.eventmanager.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenDto> authUser(@RequestBody @Valid AuthDto request) {
        log.debug("authenticating user by login: {}", request.login());
        var jwtToken = authService.authUser(request.login(), request.password());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtTokenDto(jwtToken));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterDto request) {
        log.debug("registering user by login: {}, age: {}", request.login(), request.age());
        var user = userService.createUser(request.login(), request.password(), request.age());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        log.debug("getting user by id: {}", userId);
        var user = userService.getUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserMapper.toDto(user));
    }
}
