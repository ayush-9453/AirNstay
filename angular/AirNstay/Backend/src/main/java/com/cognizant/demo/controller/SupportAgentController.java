package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.exception.TicketNotFoundException;
import com.cognizant.demo.service.AdminService; // Reusing AdminService for now
import com.cognizant.demo.service.SupportService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/supportAgent")
public class SupportAgentController {

    @Autowired
    private SupportService service; 

    @GetMapping("/tickets/{agentId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByAgentId(@PathVariable String agentId) {
        // This method will be implemented in AdminService (or a new SupportService)
        return service.getTicketsByAgentId(agentId); 
    }
    
    @PatchMapping("/supportTickets/complete/{id}")
    public ResponseEntity<SupportTicket> completeTicket(@PathVariable String id) throws TicketNotFoundException {
        return service.completeTicket(id);
    }
}