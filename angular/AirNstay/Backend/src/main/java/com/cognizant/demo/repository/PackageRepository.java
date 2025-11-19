package com.cognizant.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.Packages;

public interface PackageRepository extends JpaRepository<Packages,Integer> {
	public List<Packages> getByLocation(String location);
}
