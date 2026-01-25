package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionRequestDTO(
        @NotNull
        BigDecimal amount,

        @NotNull
        Instant dateTime,

        @NotBlank
        @Size(max = 100)
        String title,

        @Size(max = 255)
        String description,

        @NotBlank
        String type,

        @NotNull
        Long categoryId) {
}
