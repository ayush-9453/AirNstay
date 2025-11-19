package com.cognizant.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravellerDTO {
	
    private Long travellerId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
}

