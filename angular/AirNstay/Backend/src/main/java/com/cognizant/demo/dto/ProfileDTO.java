package com.cognizant.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
	
	private String uname;
	private String email;
	private Long contactNumber;
	private String Gender;
}
