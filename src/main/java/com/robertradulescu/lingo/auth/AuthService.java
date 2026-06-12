package com.robertradulescu.lingo.auth;

import com.robertradulescu.lingo.security.JwtService;
import com.robertradulescu.lingo.user.User;
import com.robertradulescu.lingo.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // No permito dos cuentas con el mismo email.
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException(request.email());
        }
        User user = new User();
        user.setEmail(request.email());
        // Guardo el HASH de la contraseña, nunca la contraseña en claro.
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return buildToken(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Mismo error si el email no existe o si la contraseña es incorrecta:
        // así un atacante no puede averiguar qué emails están registrados.
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);
        // matches() vuelve a hashear la contraseña recibida y la compara con la guardada.
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return buildToken(user);
    }

    private AuthResponse buildToken(User user) {
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000);
    }
}
