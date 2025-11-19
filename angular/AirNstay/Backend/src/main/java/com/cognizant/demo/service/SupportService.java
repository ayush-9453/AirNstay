package com.cognizant.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.exception.TicketNotFoundException;
import com.cognizant.demo.repository.SupportTicketRepository;
// You might need UsersRepository later, but only ticketRepo is needed for ticket operations
// import com.cognizant.demo.repository.UsersRepository; 


@Service
public class SupportService {

    @Autowired
    private SupportTicketRepository ticketRepo;

    public ResponseEntity<List<SupportTicket>> getTicketsByAgentId(String agentId) {
        // Use the new repository method (findByAgent) to filter tickets
        List<SupportTicket> tickets = ticketRepo.findByAgent(agentId);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    public ResponseEntity<SupportTicket> completeTicket(String ticketId) throws TicketNotFoundException {
        SupportTicket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));

        ticket.setStatus("Completed");
        ticket.setCompleted(true); // Assuming 'completed' boolean field exists

        ticketRepo.save(ticket);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
}