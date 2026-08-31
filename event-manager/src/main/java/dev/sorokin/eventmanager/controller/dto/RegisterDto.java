package dev.sorokin.eventmanager.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDto(

    @NotBlank(message = "Login cannot be empty")
    String login,

    @NotBlank(message = "Password cannot be empty")
    String password,

    @NotNull
    @Min(value = 0, message = "Age must be greater than zero")
    Integer age
) {}
