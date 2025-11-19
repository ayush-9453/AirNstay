package com.cognizant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.ProfileDTO;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoUserFoundException;
import com.cognizant.demo.service.userProfileService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {
	
	@Autowired
	private userProfileService service;

	@GetMapping("/{userId}")
	public ResponseEntity<ProfileDTO>getUserDetails(@PathVariable String userId) throws NoUserFoundException{
	
		return service.getData(userId);
		
	}
	
	@PatchMapping("/{userId}")
	public ResponseEntity<ProfileDTO>updateUserDetails(@PathVariable String userId, @RequestBody Users user) throws NoUserFoundException{
		
		return service.updateUser(userId,user);
	}
}
