package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketCreationException;
import com.cognizant.demo.service.SupportTicketService;

@RestController
@RequestMapping("/support")
@CrossOrigin(origins = "http://localhost:4200")
public class SupportTicketController {

    @Autowired
    private SupportTicketService service;

    @PostMapping("/raise")
    public ResponseEntity<SupportTicket> raiseTicket(@RequestBody SupportTicket ticket) throws TicketCreationException {
        return service.raiseTicket(ticket);
    } 
    
    @GetMapping("/getMyTickets/{userId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByUserId(@PathVariable String userId) {
        return service.getTicketsByUserID(userId);
    }
}