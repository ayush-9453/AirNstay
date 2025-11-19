package com.cognizant.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
	
	private String role;
	private String uname;
	private String email;
}
