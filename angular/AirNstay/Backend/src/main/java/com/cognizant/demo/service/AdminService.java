package com.cognizant.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cognizant.demo.dto.AdminDTO;
import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketNotFoundException;
import com.cognizant.demo.repository.SupportTicketRepository;
import com.cognizant.demo.repository.UsersRepository;
import com.cognizant.demo.serviceImp.IAdminService;

@Service
public class AdminService implements IAdminService {

	@Autowired
	private UsersRepository repo;

	@Autowired
	private SupportTicketRepository ticketRepo;

	@Autowired
	private ModelMapper model;

	public AdminDTO convertToDto(Users user) {
		return model.map(user, AdminDTO.class);
	}

	public Users convertToEntity(AdminDTO adminDTO) {
		return model.map(adminDTO, Users.class);
	}

	@Override
	public ResponseEntity<List<AdminDTO>> getUsersData() throws NoDataFoundException {
		List<Users> users = repo.findAll();
		if (users.isEmpty()) {
			throw new NoDataFoundException("No Data Found.");
		} else {
			List<AdminDTO> adminDto = users.stream().map(this::convertToDto).toList();

			return new ResponseEntity<>(adminDto, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<AdminDTO> updateUserData(Users user) throws NoDataFoundException {

		Optional<Users> userObjOp = repo.findById(user.getUserID());

		if (userObjOp.isPresent()) {
			Users exsistingUser = userObjOp.get();

			user.setPassword(exsistingUser.getPassword());
			user.setGender(exsistingUser.getGender());

			Users updatedUser = repo.save(user);
			return new ResponseEntity<>(convertToDto(updatedUser), HttpStatus.OK);
		} else {
			throw new NoDataFoundException("No User Found.");
		}
	}

	public ResponseEntity<String> deleteUser(String id) throws NoDataFoundException {

		Optional<Users> userObjOp = repo.findById(id);

		if (userObjOp.isPresent()) {
			repo.deleteById(id);
			return new ResponseEntity<>("User Deleted Successfully...", HttpStatus.OK);
		} else {
			throw new NoDataFoundException("User Detials Not Found.");
		}
	}

	@Override
	public ResponseEntity<List<SupportTicket>> getAllSupportTickets() {
		List<SupportTicket> tickets = ticketRepo.findAll();
		return new ResponseEntity<>(tickets, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SupportTicket> assignAgentToTicket(String ticketId, String agent) throws TicketNotFoundException {
		SupportTicket ticket = ticketRepo.findById(ticketId)
				.orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));

		ticket.setAgent(agent);
		ticket.setStatus("Assigned");
		ticket.setAgentAssigned(true);

		ticketRepo.save(ticket);
		return new ResponseEntity<>(ticket, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SupportTicket> completeTicket(String ticketId) throws TicketNotFoundException {
		SupportTicket ticket = ticketRepo.findById(ticketId)
				.orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + ticketId));

		ticket.setStatus("Completed");
		ticket.setCompleted(true);

		ticketRepo.save(ticket);
		return new ResponseEntity<>(ticket, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<List<Map<String, String>>> getAllAgentsWithNames() {
	    List<Users> agents = repo.findByRole("Support Agent");

	    List<Map<String, String>> agentList = agents.stream()
	        .map(agent -> Map.of(
	            "userID", agent.getUserID(),
	            "name", agent.getUname()
	        ))
	        .collect(Collectors.toList());

	    return new ResponseEntity<>(agentList, HttpStatus.OK);
	}

}
