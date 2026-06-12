package com.robertradulescu.lingo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // Lo usaré en el login para buscar al usuario por su email.
    Optional<User> findByEmail(String email);

    // Para comprobar en el registro si el email ya está cogido.
    boolean existsByEmail(String email);
}