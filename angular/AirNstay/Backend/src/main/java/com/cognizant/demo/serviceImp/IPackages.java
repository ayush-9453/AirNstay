package com.cognizant.demo.serviceImp;

import java.util.*;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.entity.Packages;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IPackages {
	public ResponseEntity<List<PackagesDTO>> getData() throws NoDataFoundException;
	public ResponseEntity<List<PackagesDTO>> getByLocation(String location) throws NoDataFoundException;
	public ResponseEntity<Packages> insertData(Packages pkg);
	public ResponseEntity<String> deleteData(int id) throws NoDataFoundException;
	public ResponseEntity<Packages> updateData(Packages pkg);

}
