package dev.sorokin.eventmanager.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record RequestEventDto(

        @NotNull
        @FutureOrPresent(message = "Date must be in present or future")
        OffsetDateTime date,

        @NotNull
        @Min(value = 60, message = "Duration must be greater than or equal to 60")
        Integer duration,

        @NotNull
        @Min(value = 1, message = "Cost must be greater than or equal to 1")
        Integer cost,

        @NotNull
        @Min(value = 1, message = "Max places must be greater than or equal to 1")
        Integer maxPlaces,

        @NotNull
        Long locationId,

        @NotBlank
        String name
) {}