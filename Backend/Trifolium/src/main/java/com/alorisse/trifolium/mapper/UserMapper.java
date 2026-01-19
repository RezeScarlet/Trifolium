package com.alorisse.trifolium.mapper;

import com.alorisse.trifolium.model.dto.CreateUserRequestDTO;
import com.alorisse.trifolium.model.dto.LoginUserRequestDTO;
import com.alorisse.trifolium.model.dto.ProviderLoginUserRequestDTO;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequestDTO dto) {
        User entity = new User();
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        return entity;
    }

    public User toEntity(LoginUserRequestDTO dto) {
        User entity = new User();
        entity.setEmail(dto.email());
        return entity;
    }

    public User toEntity(ProviderLoginUserRequestDTO dto) {
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
