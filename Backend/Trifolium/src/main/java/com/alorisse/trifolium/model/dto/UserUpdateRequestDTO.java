package com.alorisse.trifolium.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO (
        @Size(min = 3, max = 25) String username,
        @Email String email,
        @Size(min = 8) String password,
        String oldPassword,
        @Size(min = 3, max = 3) String currency
) {}
