package org.meena.treeforum.hierarchical_forum.service;

import org.meena.treeforum.hierarchical_forum.dto.RegisterDto;
import org.meena.treeforum.hierarchical_forum.model.User;
import org.meena.treeforum.hierarchical_forum.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername()) ||
                userRepository.existsByEmail(registerDto.getEmail())) {
            return null;
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        return userRepository.save(user);
    }
}
