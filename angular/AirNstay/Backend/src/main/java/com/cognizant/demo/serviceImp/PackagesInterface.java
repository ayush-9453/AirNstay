package com.cognizant.demo.serviceImp;

import java.util.*;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.exception.NoDataFoundException;

public interface PackagesInterface {
	public ResponseEntity<List<PackagesDTO>> getData() throws NoDataFoundException;
	public ResponseEntity<List<PackagesDTO>> getByLocation(String location) throws NoDataFoundException;
	public ResponseEntity<PackagesDTO> getPackageById(int id) throws NoDataFoundException;
	public ResponseEntity<PackagesDTO> insertData(PackagesDTO pkgdto);
	public ResponseEntity<String> deleteData(int id) throws NoDataFoundException;
	public ResponseEntity<PackagesDTO> updateData(PackagesDTO pkgdto);
	

}
