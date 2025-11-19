package com.cognizant.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.serviceImp.PackagesInterface;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/packages")
public class PackageController {
	
	@Autowired
	private PackagesInterface service;
	
	@GetMapping("/allData")
	public ResponseEntity<List<PackagesDTO>> getAllPackages() throws NoDataFoundException{
		return service.getData();
	}
	
	@GetMapping("/{location}")
	public ResponseEntity<List<PackagesDTO>> getPackageByLocation(@PathVariable String location) throws NoDataFoundException{
		return service.getByLocation(location);
	}
	
	@PostMapping("/insert")
	public ResponseEntity<PackagesDTO> insertPackages(@Valid @RequestBody PackagesDTO pkg) {
		return service.insertData(pkg);
	}
	
	@PutMapping("/update")
	public ResponseEntity<PackagesDTO> updatePackages(@Valid @RequestBody PackagesDTO pkg){
		return service.updateData(pkg);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deletePackages(@PathVariable int id) throws NoDataFoundException{
		return service.deleteData(id);
	}
	
	@GetMapping("getPackagesById/{id}")
	public ResponseEntity<PackagesDTO> getPackageById(@PathVariable int id) throws NoDataFoundException{
		return service.getPackageById(id);
	}

}
