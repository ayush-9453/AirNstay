import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router'; // 👈 NEW IMPORT

// Assuming these imports exist in your project structure
import { RaiseTicketDataService } from '../raise-ticket-data.service'; // The service containing ticket methods
import SupportTicket from '../SupportTicket'; // Your ticket model
import { AuthenticationService } from '../authentication.service';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-support-dashboard',
  standalone : false,
  templateUrl: './support-dashboard.component.html',
  styleUrls: ['./support-dashboard.component.css']
})
export class SupportDashboardComponent implements OnInit {

  // We only need tickets assigned to the agent
  SupportTicketData: SupportTicket[] = []; 
  FilteredTicketData: SupportTicket[] = [];

  // Agent's ID retrieved from local storage (or wherever you store it)
  currentAgentID: string = ''; 

  // Inject the Router in the constructor 👈 CHANGED CONSTRUCTOR
  constructor(
    private ticketService: RaiseTicketDataService,
    private router: Router,
    private auth : AuthenticationService,
    private toastService : ToastService
  ) { }

  ngOnInit() {
    // 1. Get the current agent's ID from local storage
    this.currentAgentID = localStorage.getItem('userID') || '';
    
    // 2. Fetch and filter tickets
    this.getAssignedTickets();
  }

  getAssignedTickets() {
    console.log("Fetching assigned tickets for agent:", this.currentAgentID);

    // Call the service method that fetches ALL tickets (like your previous admin call)
    this.ticketService.getTicketsByAgentId(this.currentAgentID).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status === 200 && response.body) {
          this.SupportTicketData = response.body.map((ticket: any) => ({
            ...ticket,
            // Add properties needed for UI state management
            completed: ticket.status === 'Completed'
          }));
        } else {
          console.log("Error Fetching Data or Empty Response");
        }
      },
      error: (err) => console.error('Error fetching tickets:', err)
    });
  }

  
  completeTicket(ticket: any) {
    // Confirm before completing (Optional but recommended)
    if (!confirm(`Are you sure you want to mark Ticket #${ticket.ticketId} as COMPLETE?`)) {
      return;
    }

    // Use the existing service method to mark the ticket as complete
    this.ticketService.completeTicket(ticket.ticketId).subscribe({
      next: () => {
        // Update the local ticket object state
        ticket.status = 'Completed';
        ticket.completed = true;
        console.log(`Ticket ${ticket.ticketId} completed successfully.`);
      },
      error: err => {
        this.toastService.showError(err.error);
      }
    });
  }

  logout() {
    console.log("Logging out agent:", this.currentAgentID);
    this.auth.logout();
  }
}