package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password) {
}
