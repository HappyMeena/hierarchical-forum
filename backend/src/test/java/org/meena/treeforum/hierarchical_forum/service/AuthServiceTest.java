package org.meena.treeforum.hierarchical_forum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meena.treeforum.hierarchical_forum.dto.RegisterDto;
import org.meena.treeforum.hierarchical_forum.model.User;
import org.meena.treeforum.hierarchical_forum.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterDto createValidRegisterDto() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("ValidUser123");
        dto.setEmail("valid@example.com");
        dto.setPassword("ValidPass1!@");
        return dto;
    }

    @Test
    void register_shouldSuccessfullyRegisterNewUserWithValidDto() {
        // Arrange
        RegisterDto registerDto = createValidRegisterDto();
        User expectedUser = new User();
        expectedUser.setUsername(registerDto.getUsername());
        expectedUser.setEmail(registerDto.getEmail());
        expectedUser.setPassword("encodedPassword");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // Act
        User result = authService.register(registerDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("ValidUser123");
        assertThat(result.getEmail()).isEqualTo("valid@example.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).existsByUsername("ValidUser123");
        verify(userRepository).existsByEmail("valid@example.com");
        verify(passwordEncoder).encode("ValidPass1!@");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldReturnNullWhenUsernameExists() {
        // Arrange
        RegisterDto registerDto = createValidRegisterDto();
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Act
        User result = authService.register(registerDto);

        // Assert
        assertThat(result).isNull();
        verify(userRepository).existsByUsername("ValidUser123");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldReturnNullWhenEmailExists() {
        // Arrange
        RegisterDto registerDto = createValidRegisterDto();
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act
        User result = authService.register(registerDto);

        // Assert
        assertThat(result).isNull();
        verify(userRepository).existsByUsername("ValidUser123");
        verify(userRepository).existsByEmail("valid@example.com");
        verify(userRepository, never()).save(any());
    }
}