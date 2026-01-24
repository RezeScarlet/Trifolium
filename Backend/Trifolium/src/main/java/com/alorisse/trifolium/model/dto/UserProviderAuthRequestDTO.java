package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProviderAuthRequestDTO(
        @NotBlank String token,
        @NotBlank String provider) {
}
