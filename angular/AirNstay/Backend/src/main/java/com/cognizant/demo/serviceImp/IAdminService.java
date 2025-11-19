package com.cognizant.demo.serviceImp;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.AdminDTO;
import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketNotFoundException;

public interface IAdminService {

	public ResponseEntity<List<AdminDTO>> getUsersData() throws NoDataFoundException;
	public ResponseEntity<AdminDTO> updateUserData(Users user) throws NoDataFoundException;
	public ResponseEntity<String> deleteUser(String id)throws NoDataFoundException;
    ResponseEntity<List<SupportTicket>> getAllSupportTickets();
    ResponseEntity<SupportTicket> assignAgentToTicket(String ticketId, String agent) throws TicketNotFoundException;
    ResponseEntity<SupportTicket> completeTicket(String ticketId) throws TicketNotFoundException;
    ResponseEntity<List<Map<String, String>>> getAllAgentsWithNames();
}
