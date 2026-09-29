package dev.sorokin.eventnotificator.controller.dto;

public record ChangeItem(
        String field,
        Object oldValue,
        Object newValue
) {}