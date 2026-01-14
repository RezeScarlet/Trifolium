package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank String title,
        @NotBlank String color,
        @NotBlank String icon) {
}
