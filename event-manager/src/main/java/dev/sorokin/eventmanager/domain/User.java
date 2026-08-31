package dev.sorokin.eventmanager.domain;

public record User(
        Long id,
        String login,
        String pass,
        Integer age,
        String role
) {}
