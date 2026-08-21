package dev.sorokin.eventmanager.controller.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(

    @Nullable
    Long id,

    @NotBlank(message = "Name cannot be empty")
    String name,

    @NotBlank(message = "Address cannot be empty")
    String address,

    @NotNull
    @Min(value = 5, message = "Capacity must be greater than or equal to 5")
    Integer capacity,

    @Nullable
    String description
) {}
