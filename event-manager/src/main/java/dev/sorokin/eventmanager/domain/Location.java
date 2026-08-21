package dev.sorokin.eventmanager.domain;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {}
