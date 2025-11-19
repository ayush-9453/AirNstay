package com.cognizant.demo.serviceImp;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.AuthResponseDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.InvalidCredentailsException;
import com.cognizant.demo.exception.NoUserFoundException;

public interface ILoginService {

	public ResponseEntity<AuthResponseDTO> loginUser(Users user) throws NoUserFoundException, InvalidCredentailsException;
	
}
