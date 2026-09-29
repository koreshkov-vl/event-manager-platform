package dev.sorokin.eventnotificator.infrastructure.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserGetter {

    public Long getUserIdFromJwt() {
        var user = (UserFromJwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.id() != null) {
            return user.id();
        } else {
            throw new RuntimeException("User id is null in jwt");
        }
    }
}
