package com.robertradulescu.lingo.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // El email es único: hará de identificador de login.
    @Column(nullable = false, unique = true)
    private String email;

    // Guardo SIEMPRE el hash (BCrypt), nunca la contraseña en texto plano.
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Rol del usuario. De momento solo "USER"; servirá para permisos más adelante.
    @Column(nullable = false)
    private String role = "USER";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}