package com.cognizant.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.AdminDTO;
import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketNotFoundException;
import com.cognizant.demo.service.AdminService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/adminDashboard")
public class AdminController {

	@Autowired
	private AdminService service;
	
	@GetMapping
	public ResponseEntity<List<AdminDTO>> getData() throws NoDataFoundException{
		return service.getUsersData();
	}
   
    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateData(@PathVariable String id, @Valid @RequestBody Users user) throws NoDataFoundException {
        user.setUserID(id);
        return service.updateUserData(user);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws NoDataFoundException{
    	return service.deleteUser(id);    	
    }
    

    @GetMapping("/supportTickets")
    public ResponseEntity<List<SupportTicket>> getAllSupportTickets() {
        return service.getAllSupportTickets();
    }

    @PatchMapping("/supportTickets/assign/{id}")
    public ResponseEntity<SupportTicket> assignAgent(@PathVariable String id, @RequestBody Map<String, String> payload) throws TicketNotFoundException {
        return service.assignAgentToTicket(id, payload.get("agent"));
    }
  
    @GetMapping("/agents")
    public ResponseEntity<List<Map<String, String>>> getAllAgentsWithNames() {
        return service.getAllAgentsWithNames();
    }

}
