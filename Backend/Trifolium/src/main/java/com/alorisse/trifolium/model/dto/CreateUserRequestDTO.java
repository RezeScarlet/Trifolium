package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(
        @NotBlank @Size(min = 3, max = 25) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password) {
}
