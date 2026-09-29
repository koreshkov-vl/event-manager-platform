package dev.sorokin.kafka.dto;

public record ChangeItem(
        String field,
        Object oldValue,
        Object newValue
) {}
