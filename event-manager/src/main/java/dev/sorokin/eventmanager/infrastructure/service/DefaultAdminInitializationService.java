package dev.sorokin.eventmanager.infrastructure.service;

import dev.sorokin.eventmanager.domain.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializationService {

    @Value("${default.admin.login}")
    private String defaultLogin;

    @Value("${default.admin.pass}")
    private String defaultPass;

    @Value("${default.admin.age}")
    private Integer defaultAge;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultAdminInitializationService(UserService userService, PasswordEncoder passwordEncod) {
        this.userService = userService;
        this.passwordEncoder = passwordEncod;
    }

    @PostConstruct
    public void init() {
        if (!userService.existsByLogin(defaultLogin)) {
            userService.createAdmin(
                    defaultLogin,
                    defaultPass,
                    defaultAge
            );
        }
    }
}
