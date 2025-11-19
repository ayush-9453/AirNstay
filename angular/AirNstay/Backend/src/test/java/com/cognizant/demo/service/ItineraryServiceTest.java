package com.cognizant.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.ItineraryDTO;
import com.cognizant.demo.entity.DayItinerary;
import com.cognizant.demo.entity.Itinerary;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.ItineraryRepository;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {
	
	@Mock
	ItineraryRepository repo;
	
	@Mock
	private ModelMapper mapper;
	
	@InjectMocks
	private ItineraryService service;
	
	
	private Itinerary entity;
	private ItineraryDTO dto;
	
	@BeforeEach
	void setup() {
		entity = new Itinerary(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner")));
		dto = new ItineraryDTO(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner")));
	}
	
	@Test
	void testGetAllData_Success() throws NoDataFoundException {
	     when(repo.findAll()).thenReturn(List.of(entity));
	     when(mapper.map(entity, ItineraryDTO.class)).thenReturn(dto);

	     ResponseEntity<List<ItineraryDTO>> response = service.getAllItineraries();

	     assertEquals(202, response.getStatusCode().value());
	     assertEquals(1, response.getBody().get(0).getDayItinerary().size());
	     assertEquals("Day1", response.getBody().get(0).getDayItinerary().get(0).getTitle());

	    }
	
	@Test
	void testGetById_Success() throws NoDataFoundException {
		when(repo.findById(1)).thenReturn(Optional.of(entity));
	    when(mapper.map(entity, ItineraryDTO.class)).thenReturn(dto);

	    ResponseEntity<ItineraryDTO> response = service.getItineraryById(1);

	    assertEquals(202, response.getStatusCode().value());
		assertEquals("Day1", response.getBody().getDayItinerary().get(0).getTitle());
	}

	@Test
	void testCreateItinerary() {
		when(mapper.map(dto, Itinerary.class)).thenReturn(entity);
		when(repo.save(entity)).thenReturn(entity);
		when(mapper.map(entity, ItineraryDTO.class)).thenReturn(dto);

		ResponseEntity<ItineraryDTO> response = service.createItinerary(dto);

	    assertEquals(200, response.getStatusCode().value());
	    assertEquals("Day1", response.getBody().getDayItinerary().get(0).getTitle());
	}
	
	@Test
	void testdeleteItinerary() {
	
		
		ResponseEntity<String> response = service.deleteItinerary(1);
		
		 assertEquals(200, response.getStatusCode().value());
	     assertEquals("Delete Successfull", response.getBody());
	     verify(repo).deleteById(1);
		
	}
}
