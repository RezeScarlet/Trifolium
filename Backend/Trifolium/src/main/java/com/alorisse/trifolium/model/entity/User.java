package com.alorisse.trifolium.model.entity;

import com.alorisse.trifolium.model.enums.AuthProvider;
import jakarta.persistence.*;

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password")
    private byte[] password;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider provider;

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public AuthProvider getProvider() {
        return this.provider;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }
}