package com.cognizant.demo.service;

import java.util.List;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketCreationException;
import com.cognizant.demo.repository.SupportTicketRepository;
import com.cognizant.demo.serviceImp.ISupportTicketService;

@Service
public class SupportTicketService implements ISupportTicketService {

    @Autowired
    private SupportTicketRepository repo;

    @Override
    public ResponseEntity<SupportTicket> raiseTicket(SupportTicket ticket) throws TicketCreationException {
        if (repo.existsById(ticket.getTicketId())) {
            throw new TicketCreationException("Ticket ID already exists: " + ticket.getTicketId());
        }

        SupportTicket savedTicket = repo.save(ticket);
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<SupportTicket>> getTicketsByUserID(String userID) {

        List<SupportTicket> tickets = repo.getTicketsByUserID(userID);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    
    }
}

