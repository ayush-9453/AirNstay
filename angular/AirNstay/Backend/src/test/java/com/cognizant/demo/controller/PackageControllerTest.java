package com.cognizant.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.serviceImp.PackagesInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PackageControllerTest {

    @Autowired
    private MockMvc mock;

    @MockitoBean
    private PackagesInterface service;

	
	@Autowired
	private ObjectMapper objectMapper;
	
    @Test
    void getAllPackagesTest() throws Exception {
        List<PackagesDTO> mockList = List.of(
            new PackagesDTO(1, "Beach Holiday", "maldives", 5000, "5 days", "url/img.jpg", "Flights, Stay", List.of("Flight", "Hotel"), "AG001")
        );
        ResponseEntity<List<PackagesDTO>> responseEntity = new ResponseEntity<>(mockList, HttpStatus.OK);

        when(service.getData()).thenReturn(responseEntity);

        mock.perform(get("/packages/allData"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value("maldives"));
    }

    @Test
    void getPackageByLocation_Success() throws Exception {
        String location = "goa";
        List<PackagesDTO> mockList = List.of(
            new PackagesDTO(1, "Goa Getaway", "goa", 23000, "3D/2N", "Packages_img", "meals and city tour", List.of("Meals", "Hotel"), "A0001")
        );
        ResponseEntity<List<PackagesDTO>> responseEntity = new ResponseEntity<>(mockList, HttpStatus.OK);

        when(service.getByLocation(location)).thenReturn(responseEntity);

        mock.perform(get("/packages/{location}", location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value("goa"));
    }
    
    @Test 
    void insertPackage_Success() throws Exception{
    	PackagesDTO mockdto = new PackagesDTO(1, "Beach Holiday", "Maldives", 5000, "5 days", "url/img.jpg", "Flights, Stay", List.of("Flight", "Hotel"), "AG001");
    	
    	ResponseEntity<PackagesDTO> responseEntity = new ResponseEntity<>(mockdto,HttpStatus.OK);
    	
    	when(service.insertData(mockdto)).thenReturn(responseEntity);
    	
    	mock.perform(post("/packages/insert").contentType("application/json")
                .content(objectMapper.writeValueAsString(mockdto)))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.location").value("Maldives"));
    	
    }
    
    @Test 
    void deletePackageTest() throws Exception{
    	int id = 1;
    	new PackagesDTO(1, "Beach Holiday", "Maldives", 5000, "5 days", "url/img.jpg", "Flights, Stay", List.of("Flight", "Hotel"), "AG001");
    	
    	ResponseEntity<String> reponseEntity = new ResponseEntity<>("",HttpStatus.OK);
    	
    	when(service.deleteData(1)).thenReturn(reponseEntity);
    	
    	mock.perform(delete("/packages/delete/{id}", id))
        .andExpect(status().isOk());

    }
}
