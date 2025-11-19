package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketCreationException;

public interface ISupportTicketService {
	ResponseEntity<SupportTicket> raiseTicket(SupportTicket ticket) throws TicketCreationException;

	ResponseEntity<List<SupportTicket>> getTicketsByUserID(String userID);

}
