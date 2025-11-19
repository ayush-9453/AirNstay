package com.cognizant.demo.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.cognizant.demo.dto.PackagesDTO;
import com.cognizant.demo.entity.Packages;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.PackageRepository;
import com.cognizant.demo.serviceImp.PackagesInterface;

@Service
public class PackageService implements PackagesInterface {
	
	@Autowired
	private PackageRepository repo;
	
	@Autowired
	private ModelMapper model;
	
	public PackagesDTO convertToDto(Packages packages) {
	        return model.map(packages, PackagesDTO.class);
	    }

	public Packages convertToEntity(PackagesDTO packagesDTO) {
	        return model.map(packagesDTO, Packages.class);
	  }

	@Override
	public ResponseEntity<List<PackagesDTO>> getData() throws NoDataFoundException {
		List<Packages> pkg = repo.findAll();
		if(pkg.isEmpty()) {
			throw new NoDataFoundException("No Data found....");
		}else {
			List<PackagesDTO> dtoList = pkg.stream().map(this::convertToDto).toList();
			return new ResponseEntity<>(dtoList,HttpStatus.ACCEPTED);
		}
	}
	
	@Override
	public ResponseEntity<List<PackagesDTO>> getByLocation(String location) throws NoDataFoundException {
		List<Packages> pkg = repo.getByLocation(location);
		if(!pkg.isEmpty()) {
			List<PackagesDTO> dtoList = pkg.stream().map(this:: convertToDto).toList();
			return new ResponseEntity<>(dtoList,HttpStatus.ACCEPTED);
		}
		else {
			throw new NoDataFoundException("No Data Found with this locaiton");
		}
	}
	

	@Override
	public ResponseEntity<PackagesDTO> insertData(PackagesDTO pkgdto) {
		Packages pkg = convertToEntity(pkgdto);
	    Packages savedPkg = repo.save(pkg);
	    return new ResponseEntity<>(convertToDto(savedPkg), HttpStatus.CREATED);	
	}

	@Override
	public ResponseEntity<PackagesDTO> updateData(PackagesDTO pkgdto) {
		if(repo.existsById(pkgdto.getId())) {
			Packages pkg = convertToEntity(pkgdto);
		    Packages savedPkg = repo.save(pkg);
		    return new ResponseEntity<>(convertToDto(savedPkg), HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@Override
	public ResponseEntity<String> deleteData(int id) throws NoDataFoundException {
		 if (!repo.existsById(id)) {
		        throw new NoDataFoundException("Package with ID " + id + " not found.");
		    }
	     repo.deleteById(id);
		 return new ResponseEntity<>("Package deleted successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PackagesDTO> getPackageById(int id) throws NoDataFoundException {
		Optional<Packages> packageDetailsObj=repo.findById(id);
		if(!packageDetailsObj.isEmpty()) {
			PackagesDTO packageDto = convertToDto(packageDetailsObj.get());
			return new ResponseEntity<>(packageDto,HttpStatus.OK);
		}
		else {
			throw new NoDataFoundException("No Data Found with this ID");
		}
	}
	
	}


	


