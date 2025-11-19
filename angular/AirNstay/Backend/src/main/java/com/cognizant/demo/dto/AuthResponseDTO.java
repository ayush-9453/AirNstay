package com.cognizant.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String userID;
    private String role;
    private String email;
}
