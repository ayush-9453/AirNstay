package com.cognizant.demo.controller;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.dto.HotelDTO;
import com.cognizant.demo.service.HotelService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class HotelControllerTest {
	

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelServiceObj;
    
    private HotelDTO hotelDTO;

    @BeforeEach
    public void setup() {
		MockitoAnnotations.openMocks(this);
		
		hotelDTO = new HotelDTO( "H001", "Vivanta Coimbatore", "img1.jpg", "img2.jpg", "img3.jpg",
	            "123 Race Course Road", "coimbatore", "641018", "Near Railway Station",
	            "https://maps.example.com/vivanta", 10, 3500.0,
	            String.join(", ", Arrays.asList("WiFi", "Pool", "Gym", "Spa")),
	            "Luxury hotel in the heart of Coimbatore", "Yes", "Included", "Flexible", "24/7", "Gold Member"
	        );
    }
    
    @Test
    public void getHotelByCity() throws Exception {
        
    	String city = "coimbatore";

        List<HotelDTO> hotelList = List.of(hotelDTO);
        ResponseEntity<List<HotelDTO>> responseObj = ResponseEntity.ok(hotelList);

        when(hotelServiceObj.getByLocation(city)).thenReturn(responseObj);

        mockMvc.perform(get("/hotel/results/{city}", city))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Vivanta Coimbatore"))
            .andExpect(jsonPath("$[0].earlyCheckIn").value("Yes"))
            .andExpect(jsonPath("$[0].city").value("coimbatore"));
  
    }
    
    @Test
    public void getHotelByHotelId() throws Exception {
        
    	String hotelId = "H001";

        List<HotelDTO> hotelList = List.of(hotelDTO);
        ResponseEntity<List<HotelDTO>> responseObj = ResponseEntity.ok(hotelList);

        when(hotelServiceObj.getHotelById("H001")).thenReturn(responseObj);

        mockMvc.perform(get("/hotel/details/{hotelId}" , hotelId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Vivanta Coimbatore"))
            .andExpect(jsonPath("$[0].earlyCheckIn").value("Yes"))
            .andExpect(jsonPath("$[0].city").value("coimbatore"));
  
    }

}
