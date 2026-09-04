package dev.sorokin.eventmanager.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(

    @NotNull
    Long id,

    @NotBlank(message = "Login cannot be empty")
    String login,

    @NotNull
    @Min(value = 0, message = "Age must be greater than zero")
    Integer age,

    @NotBlank(message = "Role cannot be empty")
    String role
) {}
