package com.cognizant.demo.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.demo.config.JwtUtil;
import com.cognizant.demo.dto.AuthResponseDTO;
import com.cognizant.demo.dto.LoginDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.InvalidCredentailsException;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.repository.UsersRepository;
import com.cognizant.demo.serviceImp.ILoginService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService implements ILoginService{

	@Autowired
	private UsersRepository repo;
	
	@Autowired
	private ModelMapper model;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public LoginDTO convertToDto(Users user) {
		return model.map(user, LoginDTO.class);
	}
	
	public Users convertToEntity(LoginDTO dto) {
		return model.map(dto, Users.class);
	}	
	
	@Override
	public ResponseEntity<AuthResponseDTO> loginUser(Users user) throws NoUserFoundException, InvalidCredentailsException {
		
		Optional<Users> userObjOp = repo.findByEmailAndRole(user.getEmail(),user.getRole());
		
		if(userObjOp.isPresent()) {
			Users foundUser = userObjOp.get();
			
			if( (user.getEmail().equals(foundUser.getEmail())) && passwordEncoder.matches(user.getPassword(), foundUser.getPassword()) && (user.getRole().equals(foundUser.getRole()))   ) {
				String token = jwtUtil.generateToken(foundUser);
				AuthResponseDTO response = new AuthResponseDTO(token, foundUser.getUserID(), foundUser.getRole(), foundUser.getEmail());
//				log.info(response.toString(), AuthResponseDTO.class);
				 return new ResponseEntity<>(response, HttpStatus.OK); 
			}
			else {
				throw new InvalidCredentailsException("Invalid Credentials");
			}
		}
		else {
			throw new NoUserFoundException("No User found with this email and role");
		}
	}
	

	
	
	
	
}
