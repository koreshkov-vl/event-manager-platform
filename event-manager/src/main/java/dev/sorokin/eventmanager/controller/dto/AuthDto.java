package dev.sorokin.eventmanager.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthDto(

    @NotBlank(message = "Login cannot be empty")
    String login,

    @NotBlank(message = "Password cannot be empty")
    String password
) {}
