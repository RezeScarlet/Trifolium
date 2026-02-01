package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.*;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Should create a User successfully")
    void shouldCreateUserSuccessfully() {
        User user = buildUser();
        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        String currency = user.getCurrency();

        String password = "password123";
        String encodedPassword = "encodedPassword123";

        user.setPassword(encodedPassword.getBytes());

        UserCreateRequestDTO request = new UserCreateRequestDTO(username, email, password, currency);

        UserResponseDTO expectedResponse = new UserResponseDTO(id, username, email, currency);

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
        assertThat(result.currency()).isEqualTo(currency);

        verify(passwordEncoder).encode(password);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should login User successfully")
    void shouldLoginUserSuccessfully() {
        User user = buildUser();
        String username = user.getUsername();
        String email = user.getEmail();

        String password = "password123";
        String encodedPassword = "encodedPassword123";

        user.setPassword(encodedPassword.getBytes());

        String token = "token123";

        UserLoginRequestDTO request = new UserLoginRequestDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn(token);

        UserLoginResponseDTO result = userService.login(request);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo(token);
        assertThat(result.username()).isEqualTo(username);
        assertThat(result.email()).isEqualTo(email);

        verify(userRepository).findByEmail(email);
        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Should update User successfully")
    void shouldUpdateUserSuccessfully() {
        User oldUser = buildUser();
        Long id = oldUser.getId();

        String oldPassword = "password123";
        String encodedOldPassword = "encodedPassword123";
        oldUser.setPassword(encodedOldPassword.getBytes());

        String newUsername = "newUsername";
        String newEmail = "newuser@email.com";
        String newCurrency = "BRL";
        String newPassword = "123Password";
        String encodedNewPassword = "encoded123Password";

        UserUpdateRequestDTO request = new UserUpdateRequestDTO(newUsername, newEmail, newPassword, oldPassword, newCurrency);

        UserResponseDTO response = new UserResponseDTO(id, newUsername, newEmail, newCurrency);

        when(passwordEncoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg()); // returns the User declared on the method args

        when(userMapper.toDTO(any(User.class))).thenReturn(response);

        UserResponseDTO result = userService.update(oldUser, request);

        assertThat(result).isNotNull();

        verify(userRepository).save(userCaptor.capture());

        User newUser = userCaptor.getValue();

        assertThat(newUser.getId()).isEqualTo(id);
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(newEmail);
        assertThat(newUser.getCurrency()).isEqualTo(newCurrency);
        assertThat(newUser.getPassword()).isEqualTo(encodedNewPassword.getBytes());

        verify(passwordEncoder).matches(oldPassword, encodedOldPassword);
        verify(userRepository).existsByEmail(request.email());
    }

    @Test
    @DisplayName("Should delete User successfully")
    void shouldDeleteUserSuccessfully() {
        User user = new User();

        userService.delete(user);

        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw exception when create user with existing email")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        User user = buildUser();

        String username = "newUsername";
        String emailAlreadyInUse = user.getEmail();
        String password = "password123";


        UserCreateRequestDTO request = new UserCreateRequestDTO(username, emailAlreadyInUse, password, null);

        when(userRepository.existsByEmail(emailAlreadyInUse)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(request));

        verify(userRepository).existsByEmail(emailAlreadyInUse);
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Should throw exception when User not found")
    void shouldThrowExceptionWhenLoginUserNotFound() {
        User user = buildUser();

        String invalidEmail = "invalid@email.com";

        String password = "password123";
        String encodedPassword = "encodedPassword123";
        user.setPassword(encodedPassword.getBytes());

        UserLoginRequestDTO request = new UserLoginRequestDTO(invalidEmail, password);

        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(request));

        verify(userRepository).findByEmail(invalidEmail);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Should throw exception when password does not match")
    void shouldThrowExceptionWhenLoginWithWrongPassword() {
        User user = buildUser();
        String email = user.getEmail();

        String encodedPassword = "encodedPassword123";
        user.setPassword(encodedPassword.getBytes());

        String invalidPassword = "invalidPassword";

        UserLoginRequestDTO request = new UserLoginRequestDTO(email, invalidPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(invalidPassword, encodedPassword)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login(request));

        verify(passwordEncoder).matches(invalidPassword, encodedPassword);
        verifyNoInteractions(tokenService);
    }

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("user@email.com");
        user.setCurrency("USD");

        return user;
    }
}