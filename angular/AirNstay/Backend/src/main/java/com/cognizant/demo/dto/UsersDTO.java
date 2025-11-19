package com.cognizant.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
	
	private String userID;
	private String uname;
	private String email;
	private Long contactNumber;
	private String password;
	private String role;
	private String gender;
}
