
package com.cognizant.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDTO {
		
    private Long contactId;
    private String country;
    private String phone;
    private String email;
}

