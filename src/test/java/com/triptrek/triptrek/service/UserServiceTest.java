package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        userService = new UserService();
        userService.userRepository = userRepository;
        userService.passwordEncoder = passwordEncoder;
        userService.jwtService = jwtService;
    }

    @Test
    void testRegisterUser_success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plain");

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User saved = userService.registerUser(user);

        assertEquals("test@example.com", saved.getEmail());
        assertEquals("encoded", saved.getPassword());
    }

    @Test
    void testRegisterUser_emailAlreadyUsed() {
        User existing = new User();
        existing.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(existing);

        User user = new User();
        user.setEmail("test@example.com");

        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.findUserByEmail("test@example.com");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testFindUserByToken_success() {
        User user = new User();
        user.setEmail("test@example.com");
        when(jwtService.extractEmail("token")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.findUserByToken("token");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testFindUserByToken_invalidToken() {
        when(jwtService.extractEmail("badtoken")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.findUserByToken("badtoken"));
    }
}