package com.alorisse.trifolium.model.dto;

public record UserLoginResponseDTO(
        String token,
        String username,
        String email

) {
}
