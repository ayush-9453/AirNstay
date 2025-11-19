package com.cognizant.demo.serviceImp;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.RegisterDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.UserAlreadyExistsException;

public interface IRegisterService {
	public ResponseEntity<RegisterDTO> registerUser(Users user) throws UserAlreadyExistsException;
}
