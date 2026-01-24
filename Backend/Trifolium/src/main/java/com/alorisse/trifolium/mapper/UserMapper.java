package com.alorisse.trifolium.mapper;

import com.alorisse.trifolium.model.dto.UserCreateRequestDTO;
import com.alorisse.trifolium.model.dto.UserLoginRequestDTO;
import com.alorisse.trifolium.model.dto.UserProviderAuthRequestDTO;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserCreateRequestDTO dto) {
        User entity = new User();
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        return entity;
    }

    public User toEntity(UserLoginRequestDTO dto) {
        User entity = new User();
        entity.setEmail(dto.email());
        return entity;
    }

    public User toEntity(UserProviderAuthRequestDTO dto) {
        User entity = new User();
        entity.setProvider(AuthProvider.valueOf(dto.provider()));
        return entity;
    }

    public UserResponseDTO toDTO(User entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail()
        );
    }


}
