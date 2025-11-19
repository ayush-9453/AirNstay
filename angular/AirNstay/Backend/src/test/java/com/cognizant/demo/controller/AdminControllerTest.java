package com.cognizant.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.config.JwtUtil;
import com.cognizant.demo.config.SecurityConfig;
import com.cognizant.demo.dto.AdminDTO;
import com.cognizant.demo.entity.SupportTicket;
import com.cognizant.demo.entity.Users;
import com.cognizant.demo.exception.GlobalExceptionHandler;
import com.cognizant.demo.service.AdminService;
import com.cognizant.demo.serviceImp.ItineraryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AdminService adminService;

	@Test
	void testGetDataSuccess() throws Exception {
		List<AdminDTO> adminList = List
				.of(new AdminDTO("A100001", "Admin", "admin@cognizant.com", 9325769299L, "Admin"));

		Mockito.when(adminService.getUsersData()).thenReturn(ResponseEntity.ok(adminList));

		mockMvc.perform(get("/adminDashboard")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].userID").value("A100001")).andExpect(jsonPath("$[0].uname").value("Admin"));
	}

	@Test
	void testUpdateDataSuccess() throws Exception {
		Users user = new Users();
		user.setUserID("A100001");
		user.setUname("UpdatedAdmin");
		user.setRole("Admin");
		user.setEmail("admin@cognizant.com");

		AdminDTO updatedAdmin = new AdminDTO("A100001", "UpdatedAdmin", "admin@cognizant.com", 9325769299L, "Admin");

		Mockito.when(adminService.updateUserData(Mockito.any(Users.class))).thenReturn(ResponseEntity.ok(updatedAdmin));

		mockMvc.perform(put("/adminDashboard/A100001").contentType("application/json")
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(jsonPath("$.uname").value("UpdatedAdmin"));
	}

	@Test
	void testDeleteUserSuccess() throws Exception {
		Mockito.when(adminService.deleteUser("A100001")).thenReturn(ResponseEntity.ok("User deleted successfully"));

		mockMvc.perform(delete("/adminDashboard/A100001")).andExpect(status().isOk())
				.andExpect(content().string("User deleted successfully"));
	}

	@Test
	void testGetAllSupportTicketsSuccess() throws Exception {
		SupportTicket ticket = new SupportTicket("T1001", "U1001", "Om Jadhav", "Booking issue", new Date(), "Pending",
				"", false, false);
		List<SupportTicket> tickets = List.of(ticket);

		Mockito.when(adminService.getAllSupportTickets()).thenReturn(ResponseEntity.ok(tickets));

		mockMvc.perform(get("/adminDashboard/supportTickets")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].ticketId").value("T1001")).andExpect(jsonPath("$[0].status").value("Pending"))
				.andExpect(jsonPath("$[0].agentAssigned").value(false));
	}

	@Test
	void testAssignAgentSuccess() throws Exception {
		SupportTicket updatedTicket = new SupportTicket("T1001", "U1001", "Om Jadhav", "Booking issue", new Date(),
				"Pending", "Agent1", true, false);

		Mockito.when(adminService.assignAgentToTicket(Mockito.eq("T1001"), Mockito.eq("Agent1")))
				.thenReturn(ResponseEntity.ok(updatedTicket));

		mockMvc.perform(patch("/adminDashboard/supportTickets/assign/T1001").contentType("application/json")
				.content("{\"agent\":\"Agent1\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.agent").value("Agent1")).andExpect(jsonPath("$.agentAssigned").value(true));
	}

	@Test
	void testGetAllAgentsWithNamesSuccess() throws Exception {
		List<Map<String, String>> agents = List.of(Map.of("id", "AG001", "name", "Agent1"));

		Mockito.when(adminService.getAllAgentsWithNames()).thenReturn(ResponseEntity.ok(agents));

		mockMvc.perform(get("/adminDashboard/agents")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value("AG001")).andExpect(jsonPath("$[0].name").value("Agent1"));
	}

}
