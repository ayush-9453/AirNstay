package com.cognizant.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
	
	@Id
	@NotNull(message ="User ID cannot be null")
	private String userID;

	@NotNull(message="Name cannot be null")
	private String uname;
	
	@NotNull(message ="Email cannot be null")
	private String email;
	
	private Long contactNumber;
	
//	@NotNull(message = "Password cannot be null")
	private String password;
	
	@NotNull(message ="Role Cannot be null")
	private String role;
	
//	@NotNull(message = "gender cannot be null")
	private String gender;
	
}
