package com.cognizant.demo.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.ProfileDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class userProfileService {

	@Autowired
	private UsersRepository repo;
	
	@Autowired
	private ModelMapper model;
	
	public ProfileDTO convertToDto(Users user) {
		return model.map(user, ProfileDTO.class);
	}
	
	public ResponseEntity<ProfileDTO> getData(String userId) throws NoUserFoundException{
		Optional<Users> Obj = repo.findById(userId);
		if(Obj.isEmpty()) {
			throw new NoUserFoundException("User with this ID not Found");
		}
		else {
			
			Users user = Obj.get(); 
			ProfileDTO dto = convertToDto(user);
			
			return new ResponseEntity<>(dto,HttpStatus.OK);
		}
		
	}

	public ResponseEntity<ProfileDTO> updateUser(String userId, Users user) throws NoUserFoundException {
		Optional<Users> Obj = repo.findById(userId);
		if(Obj.isEmpty()) {
			throw new NoUserFoundException("User with this ID not found");
		}
		else {
			String newUserId = Obj.get().getUserID();
			String newPass = Obj.get().getPassword();
			String newrole = Obj.get().getRole();
			
			log.info(Obj.get().toString());
			
			user.setUserID(newUserId);
			user.setPassword(newPass);
			user.setRole(newrole);
			
			log.info(user.toString());
			ProfileDTO dto = convertToDto(repo.save(user));
			return new ResponseEntity<>(dto,HttpStatus.OK);
		}
		
	}
	
}
