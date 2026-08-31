package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.controller.dto.UserDto;
import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.persistence.entity.UserEntity;

public final class UserMapper {

    private UserMapper() {}

    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getPass(),
                entity.getAge(),
                entity.getRole().toString()
        );
    }

    public static UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }
}
