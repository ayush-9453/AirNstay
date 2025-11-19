package com.cognizant.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.Users;

public interface UsersRepository extends JpaRepository<Users,String>{

	Optional<Users> findByEmailAndRole(String email, String role);

	Optional<Users> findByEmail(String email);
	
	List<Users> findByRole(String role);

}
