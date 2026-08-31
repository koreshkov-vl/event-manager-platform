package dev.sorokin.eventmanager.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtTokenDto(

    @NotBlank(message = "Jwt token cannot be empty")
    String jwtToken
) {}
