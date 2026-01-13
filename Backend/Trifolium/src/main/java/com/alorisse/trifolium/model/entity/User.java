package com.alorisse.trifolium.model.entity;

import com.alorisse.trifolium.model.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash")
    private Byte[] password_hash;

    @Column(name = "password_hash")
    private Byte[] password_salt;

    @Column(name = "provider_id")
    private String provider_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider provider;

}