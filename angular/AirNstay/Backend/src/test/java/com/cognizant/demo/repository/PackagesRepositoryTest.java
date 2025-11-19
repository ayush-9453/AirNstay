package com.cognizant.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.Packages;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class PackagesRepositoryTest {

	@Autowired
	PackageRepository repo;
	
	@Test
	public void PackagesRepositoryFindByLocationTesting() {
//		Packages pkgObj1 = new Packages("Goa Getaway", "goa", 23000, "3D/2N", "Packages_img", "meals and city tour", List.of("Meals", "Hotel"), "A0001");
		Packages pkg = new Packages();
		pkg.setTitle("Beach Holiday");
		pkg.setLocation("Maldives");
		pkg.setPrice(5000);
		pkg.setDuration("5 days");
		pkg.setImageUrl("url/img.jpg");
		pkg.setInclusions("Flights, Stay");
		pkg.setFeatures(List.of("Flight", "Hotel"));
		pkg.setAgentId("AG001");
//		repo.save(pkgObj1);
		repo.save(pkg);
		
		List<Packages> pkgObjOp = repo.getByLocation("Maldives");
		

	    assertEquals(1, pkgObjOp.size());
	    assertEquals("Maldives", pkgObjOp.get(0).getLocation());
	}
}
