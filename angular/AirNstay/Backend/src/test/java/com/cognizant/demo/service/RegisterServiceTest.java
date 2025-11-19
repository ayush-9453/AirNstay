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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cognizant.demo.dto.RegisterDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.UserAlreadyExistsException;
import com.cognizant.demo.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest{

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService registerService;
    
    @Mock
    private UsersRepository usersRepository;
    
    @Mock
    private ModelMapper modelMapper;

    private Users user;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setEmail("john@cognizant.com");
        user.setPassword("rawPassword");
        user.setRole("Admin");

        registerDTO = new RegisterDTO();
        registerDTO.setEmail("john@cognizant.com");
        registerDTO.setRole("Admin");
    }

    @Test
    void testRegisterUserSuccess() throws UserAlreadyExistsException {
        when(usersRepository.findByEmailAndRole("john@cognizant.com", "Admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(modelMapper.map(user, RegisterDTO.class)).thenReturn(registerDTO);

        ResponseEntity<RegisterDTO> response = registerService.registerUser(user);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getEmail()).isEqualTo("john@cognizant.com");
        verify(usersRepository, times(1)).save(user);
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getUserID()).startsWith("T"); // Default prefix for non-agent roles
    }

    @Test
    void testRegisterUserAlreadyExists() {
        when(usersRepository.findByEmailAndRole("john@cognizant.com", "Admin")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> registerService.registerUser(user));
        verify(usersRepository, never()).save(any());
    }

    @Test
    void testGenerateUserIdForRoles() {
        String hotelManagerId = registerService.generateUserId("Hotel Manager");
        String travelAgentId = registerService.generateUserId("Travel Agent");
        String supportAgentId = registerService.generateUserId("Support Agent");
        String defaultId = registerService.generateUserId("Admin");

        assertThat(hotelManagerId).startsWith("HM");
        assertThat(travelAgentId).startsWith("TA");
        assertThat(supportAgentId).startsWith("SA");
        assertThat(defaultId).startsWith("T");
    }
}