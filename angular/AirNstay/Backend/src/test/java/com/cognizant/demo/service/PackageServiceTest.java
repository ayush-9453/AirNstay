package com.cognizant.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.entity.Packages;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.PackageRepository;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    private PackageRepository repo;

    @Mock
    private ModelMapper model;

    @InjectMocks
    private PackageService service;

    private Packages entity;
    private PackagesDTO dto;

    @BeforeEach
    void setup() {
        entity = new Packages(1, "Beach Holiday", "Maldives", 5000, "5 days", "url/img.jpg", "Flights, Stay",List.of("Flight", "Hotel"), "AG001");
        dto = new PackagesDTO(1, "Beach Holiday", "Maldives", 5000, "5 days", "url/img.jpg", "Flights, Stay", List.of("Flight", "Hotel"), "AG001");
    }

    @Test
    void testGetData_Success() throws NoDataFoundException {
        when(repo.findAll()).thenReturn(List.of(entity));
        when(model.map(entity, PackagesDTO.class)).thenReturn(dto);

        ResponseEntity<List<PackagesDTO>> response = service.getData();

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Maldives", response.getBody().get(0).getLocation());
    }

    @Test
    void testGetData_NoDataFound() {
        when(repo.findAll()).thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () -> service.getData());
    }

    @Test
    void testInsertData_Success() {
        when(model.map(dto, Packages.class)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(model.map(entity, PackagesDTO.class)).thenReturn(dto);

        ResponseEntity<PackagesDTO> response = service.insertData(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Maldives", response.getBody().getLocation());
    }

    @Test
    void testDeleteData_Success() throws NoDataFoundException {
        when(repo.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = service.deleteData(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Package deleted successfully", response.getBody());
        verify(repo).deleteById(1);
    }

    @Test
    void testDeleteData_NotFound() {
        when(repo.existsById(1)).thenReturn(false);

        assertThrows(NoDataFoundException.class, () -> service.deleteData(1));
    }
}
