package com.cognizant.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.AdminDTO;
import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.TicketNotFoundException;
import com.cognizant.demo.repository.SupportTicketRepository;
import com.cognizant.demo.repository.UsersRepository;


public class AdminServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private SupportTicketRepository ticketRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminService adminService;

    private Users user;
    private AdminDTO adminDTO;
    private SupportTicket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setUserID("U1001");
        user.setUname("Om");
        user.setEmail("Om@cognizant.com");
        user.setPassword("password123");
        user.setRole("Admin");

        adminDTO = new AdminDTO("U1001", "Om", "Om@cognizant.com", 9876543210L, "Admin");

        ticket = new SupportTicket("T1001", "U1001", "Om", "Booking issue", new Date(), "Pending", "", false, false);
    }

    @Test
    void testGetUsersDataSuccess() throws NoDataFoundException {
        when(usersRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, AdminDTO.class)).thenReturn(adminDTO);

        ResponseEntity<List<AdminDTO>> response = adminService.getUsersData();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getUname()).isEqualTo("Om");
    }

    @Test
    void testUpdateUserDataSuccess() throws NoDataFoundException {
        when(usersRepository.findById("U1001")).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        when(modelMapper.map(user, AdminDTO.class)).thenReturn(adminDTO);

        ResponseEntity<AdminDTO> response = adminService.updateUserData(user);

        assertThat(response.getBody().getUname()).isEqualTo("Om");
    }

    @Test
    void testDeleteUserSuccess() throws NoDataFoundException {
        when(usersRepository.findById("U1001")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = adminService.deleteUser("U1001");

        assertThat(response.getBody()).isEqualTo("User Deleted Successfully...");
        verify(usersRepository, times(1)).deleteById("U1001");
    }

   
    @Test
    void testGetAllSupportTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        ResponseEntity<List<SupportTicket>> response = adminService.getAllSupportTickets();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTicketId()).isEqualTo("T1001");
    }

    
    @Test
    void testAssignAgentToTicketSuccess() throws TicketNotFoundException {
        when(ticketRepository.findById("T1001")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(SupportTicket.class))).thenReturn(ticket);

        ResponseEntity<SupportTicket> response = adminService.assignAgentToTicket("T1001", "Agent1");

        assertThat(response.getBody().getAgent()).isEqualTo("Agent1");
        assertThat(response.getBody().isAgentAssigned()).isTrue();
    }

    @Test
    void testCompleteTicketSuccess() throws TicketNotFoundException {
        when(ticketRepository.findById("T1001")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(SupportTicket.class))).thenReturn(ticket);

        ResponseEntity<SupportTicket> response = adminService.completeTicket("T1001");

        assertThat(response.getBody().getStatus()).isEqualTo("Completed");
        assertThat(response.getBody().isCompleted()).isTrue();
    }

    @Test
    void testGetAllAgentsWithNames() {
        Users agent = new Users();
        agent.setUserID("AG001");
        agent.setUname("Agent1");
        agent.setRole("Support Agent");

        when(usersRepository.findByRole("Support Agent")).thenReturn(List.of(agent));

        ResponseEntity<List<Map<String, String>>> response = adminService.getAllAgentsWithNames();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).get("name")).isEqualTo("Agent1");
    }
}