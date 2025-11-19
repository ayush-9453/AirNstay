package com.cognizant.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cognizant.demo.config.JwtUtil;
import com.cognizant.demo.dto.AuthResponseDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.InvalidCredentailsException;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LoginService loginService;

    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setUserID("U1001");
        user.setEmail("admin@cognizant.com");
        user.setPassword("encodedPassword");
        user.setRole("Admin");
    }

    // ✅ 1. Test loginUser() success
    @Test
    void testLoginUserSuccess() throws NoUserFoundException, InvalidCredentailsException {
        Users inputUser = new Users();
        inputUser.setEmail("admin@cognizant.com");
        inputUser.setPassword("rawPassword");
        inputUser.setRole("Admin");

        when(usersRepository.findByEmailAndRole("admin@cognizant.com", "Admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("JWT_TOKEN");

        ResponseEntity<AuthResponseDTO> response = loginService.loginUser(inputUser);

        assertThat(response.getBody().getToken()).isEqualTo("JWT_TOKEN");
        assertThat(response.getBody().getEmail()).isEqualTo("admin@cognizant.com");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testLoginUserInvalidCredentials() {
        Users inputUser = new Users();
        inputUser.setEmail("admin@cognizant.com");
        inputUser.setPassword("wrongPassword");
        inputUser.setRole("Admin");

        when(usersRepository.findByEmailAndRole("admin@cognizant.com", "Admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentailsException.class, () -> loginService.loginUser(inputUser));
    }

    @Test
    void testLoginUserNoUserFound() {
        Users inputUser = new Users();
        inputUser.setEmail("unknown@cognizant.com");
        inputUser.setPassword("password123");
        inputUser.setRole("Admin");

        when(usersRepository.findByEmailAndRole("unknown@cognizant.com", "Admin")).thenReturn(Optional.empty());

        assertThrows(NoUserFoundException.class, () -> loginService.loginUser(inputUser));
    }
}
