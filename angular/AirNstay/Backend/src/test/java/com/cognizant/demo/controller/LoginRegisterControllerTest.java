package com.cognizant.demo.controller;

import com.cognizant.demo.dto.AuthResponseDTO;
import com.cognizant.demo.dto.RegisterDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.InvalidCredentailsException;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.exception.UserAlreadyExistsException;
import com.cognizant.demo.service.LoginService;
import com.cognizant.demo.service.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class LoginRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private LoginService loginService;

	@MockBean
	private RegisterService registerService;

	// Optional: If JwtUtil is used inside LoginService or RegisterService
	@MockBean
	private com.cognizant.demo.config.JwtUtil jwtUtil;

	@Test
	void UserSuccess() throws Exception {
		Users user = new Users("TA330880", "Om Jadhav", "om@gmail.com", 1231231244L, "password", "Travel Agent",
				"Male");
		RegisterDTO responseDTO = new RegisterDTO("Travel Agent", "Om Jadhav", "om@gmail.com");

		Mockito.when(registerService.registerUser(Mockito.any(Users.class))).thenReturn(ResponseEntity.ok(responseDTO));

		mockMvc.perform(post("/loginRegister/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("om@gmail.com"));
	}

	@Test
	void testLoginUserSuccess() throws Exception {
		Users user = new Users("TA330880", "Om Jadhav", "om@gmail.com", 1231231244L, "password", "Travel Agent",
				"Male");
		AuthResponseDTO authResponse = new AuthResponseDTO("Login Successful", "Travel Agent", "om@gmail.com",
				"jwt-token");

		Mockito.when(loginService.loginUser(Mockito.any(Users.class))).thenReturn(ResponseEntity.ok(authResponse));

		mockMvc.perform(post("/loginRegister/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk());

	}

	@Test
	void testRegisterUserAlreadyExists() throws Exception {
		Users user = new Users();
		user.setUserID("U1001");
		user.setEmail("om@cognizant.com");
		user.setRole("Traveller");
		user.setUname("Om Jadhav");

		Mockito.when(registerService.registerUser(Mockito.any(Users.class)))
				.thenThrow(new UserAlreadyExistsException("User already exists"));

		mockMvc.perform(post("/loginRegister/register").contentType("application/json")
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isConflict()); // 409 for already
																									// exists
	}

	@Test
	void testLoginUserInvalidCredentials() throws Exception {
		Users user = new Users("TA330880", "Om Jadhav", "om@gmail.com", 1231231244L, "wrongpass", "Travel Agent",
				"Male");

		Mockito.when(loginService.loginUser(Mockito.any(Users.class)))
				.thenThrow(new InvalidCredentailsException("Invalid credentials"));

		mockMvc.perform(post("/loginRegister/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(content().string("Invalid credentials"));
	}

}