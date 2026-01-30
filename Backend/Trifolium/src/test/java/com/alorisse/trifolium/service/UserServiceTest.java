package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.UserCreateRequestDTO;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should create a User successfully")
    void shouldCreateUserSuccessfully() {
        User user = buildUser();
        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();

        String password = "password123";
        String encodedPassword = "encodedPassword123";

        user.setPassword(encodedPassword.getBytes());

        UserCreateRequestDTO request = new UserCreateRequestDTO(username, email, password);

        UserResponseDTO expectedResponse = new UserResponseDTO(id, username, email);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(expectedResponse);
        
        UserResponseDTO result = userService.create(request);
        
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.username()).isEqualTo(username);
        assertThat(result.email()).isEqualTo(email);

        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("user@email.com");

        return user;
    }
}
