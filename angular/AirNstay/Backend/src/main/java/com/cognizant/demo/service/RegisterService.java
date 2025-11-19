package com.cognizant.demo.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.RegisterDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.UserAlreadyExistsException;
import com.cognizant.demo.repository.UsersRepository;
import com.cognizant.demo.serviceImp.IRegisterService;

@Service
public class RegisterService implements IRegisterService{
	
	@Autowired
	UsersRepository repo;
	
	@Autowired
	private ModelMapper model;
	

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	public RegisterDTO convertToDto(Users user) {
		return model.map(user, RegisterDTO.class);
	}
	
	public Users convertToEntity(RegisterDTO userDto) {
		return model.map(userDto, Users.class);
	}
	
	public String generateUserId(String role) {
	    int randomId = (int)(100000 + Math.random() * 900000);
	    
	    switch (role) {
	        case "Hotel Manager":
	            return "HM" + randomId;
	        case "Travel Agent":
	            return "TA" + randomId;
	        case "Support Agent":
	        	return "SA"+ randomId;
	        default:
	            return "T" + randomId;
	    }
	}

	@Override
	public ResponseEntity<RegisterDTO> registerUser(Users user) throws UserAlreadyExistsException {
		
		Optional<Users> userObjOp = repo.findByEmailAndRole(user.getEmail(),user.getRole());
		
		if(userObjOp.isPresent()) {
			throw new UserAlreadyExistsException("User with Same Email and Role Exists");
		}
		else {
			user.setUserID(generateUserId(user.getRole()));
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			repo.save(user);
			return new ResponseEntity<>(convertToDto(user),HttpStatus.OK);
		}
	} 
}