package com.cognizant.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
	
	private String userID;
	private String uname;
	private String email;
	private Long contactNumber;
	private String role;

}
