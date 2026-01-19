package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.CreateUserRequestDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public void createUser(CreateUserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        String hashString = passwordEncoder.encode(dto.password());
        assert hashString != null; // TODO deal with exceptions
        user.setPassword(hashString.getBytes(StandardCharsets.UTF_8));
        userRepository.save(user);
    }

    public boolean validatePassword(String password, byte[] passwordHash) {
        String hash = new String(passwordHash, StandardCharsets.UTF_8);
        
        return passwordEncoder.matches(password, hash);
    }


}
