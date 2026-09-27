package dev.sorokin.eventnotificator.infrastructure.service;

public record UserFromJwt(
    String login,
    Long id,
    String role
) {}
