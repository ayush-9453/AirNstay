package com.cognizant.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.Users;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void testFindByEmailAndRole() {
        Users user = new Users();
        user.setUserID("U1001");
        user.setEmail("admin@cognizant.com");
        user.setRole("Admin");
        user.setUname("Admin");
        user.setPassword("password123");

        Optional<Users> result = usersRepository.findByEmailAndRole("admin@cognizant.com", "Admin");

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo("Admin");
    }

    @Test
    void testFindByEmail() {
        Users user = new Users();
        user.setUserID("U1002");
        user.setEmail("om@cognizant.com");
        user.setRole("Traveller");
        user.setUname("Om Jadhav");
        user.setPassword("password123");

        usersRepository.save(user);

        Optional<Users> result = usersRepository.findByEmail("om@cognizant.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUname()).isEqualTo("Om Jadhav");
    }

    @Test
    void testFindByRole() {
    	
    	 Users user1 = new Users();
         user1.setUserID("U1002");
         user1.setEmail("om@cognizant.com");
         user1.setRole("traveler");
         user1.setUname("Om");
         user1.setPassword("password123");
         
         Users user2 = new Users();
         user2.setUserID("U1004");
         user2.setEmail("abhay@cognizant.com");
         user2.setRole("traveler");
         user2.setUname("Abhay");
         user2.setPassword("password123");
         

        usersRepository.save(user1);
        usersRepository.save(user2);

        List<Users> result = usersRepository.findByRole("traveler");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Users::getUname).contains("Om", "Abhay");
    }


    @Test
    void testFindByEmailAndRoleNotFound() {
        Optional<Users> result = usersRepository.findByEmailAndRole("unknown@cognizant.com", "Admin");
        assertThat(result).isEmpty();
    }
}