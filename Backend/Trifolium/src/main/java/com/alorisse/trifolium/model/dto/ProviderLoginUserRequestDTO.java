package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProviderLoginUserRequestDTO(
        @NotBlank String token,
        @NotBlank String provider) {
}
