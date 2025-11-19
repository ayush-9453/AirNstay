package com.cognizant.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PackageBookingDTO extends BookingDTO {
    private String packageTitle;
    private String agentId;
    private String duration;
    private String location;
}
