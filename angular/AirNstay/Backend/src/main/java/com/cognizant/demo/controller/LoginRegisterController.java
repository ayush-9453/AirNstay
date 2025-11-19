package com.cognizant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.AuthResponseDTO;
import com.cognizant.demo.dto.RegisterDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.InvalidCredentailsException;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.exception.UserAlreadyExistsException;
import com.cognizant.demo.service.LoginService;
import com.cognizant.demo.service.RegisterService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/loginRegister")
@Slf4j
public class LoginRegisterController {
	 
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private RegisterService registerService;
	
	
	@PostMapping("/register")
	public ResponseEntity<RegisterDTO> registerUser(@Valid @RequestBody Users user) throws UserAlreadyExistsException {
		return registerService.registerUser(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody Users user) throws NoUserFoundException, InvalidCredentailsException {
		return loginService.loginUser(user);
	} 
	
	
}
