import { Component } from '@angular/core';
import { RegisterService } from '../register.service';
import User from '../User';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AuthenticationService } from '../authentication.service';
import { Router } from '@angular/router';
import SupportTicket from '../SupportTicket';
import { RaiseTicketDataService } from '../raise-ticket-data.service';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {

  userDetail!: User[];
  editProfileForm!: FormGroup;
  addUserForm!: FormGroup;
  selectedRole: string = "All";
  UserbyRoles!: User[];
  userId!: string;
  userName!: string;
  dispDashboard: boolean = true;
  SupportTicketData!: SupportTicket[];

  constructor(private rest: RegisterService, private fb: FormBuilder, private authentication: AuthenticationService, private route: Router, private ticketService: RaiseTicketDataService,private toastService: ToastService) {
    this.getDataFromServer();
    this.getSupportTicketData();
    this.getAgentsList();

    // Form Group for Edit Profile
    this.editProfileForm = this.fb.group({
      id: [{ value: null, disabled: true }],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      role: [{ value: '', disabled: true }],
      contactNumber: ['', [this.contactNumberLength]]
    })

    this.addUserForm = this.fb.group({
      nName: ['',Validators.required],
      nEmail: ['',[Validators.required, Validators.email]],
      nPassword: ['',[Validators.required,Validators.minLength(8)]],
      nContactNumber : ['',[this.contactNumberLength]],
      nRole: [{value:''},Validators.required]
    })
  }

  ngOnInit() {
    // To retain the Dashboard state on page refresh
    const savedState = sessionStorage.getItem('dispDashboard');
    this.dispDashboard = savedState === 'false' ? false : true;
    this.getAgentsList();
  }

  // Edit User Record
  editRecord(userObj: User) {
    this.editProfileForm.patchValue({
      id: userObj.userID,
      name: userObj.uname,
      email: userObj.email,
      role: userObj.role,
      contactNumber: userObj.contactNumber
    })
  }

  addNewUser(){
    let nName = this.addUserForm.get(['nName'])?.value;
    let nEmail = this.addUserForm.get(['nEmail'])?.value;
    let nPassword = this.addUserForm.get(['nPassword'])?.value;
    let nRole = this.addUserForm.get(['nRole'])?.value;
    let nContactNumber = this.addUserForm.get(['nContactNumber'])?.value;
    console.log(nName,nEmail,nContactNumber);
    
    let UserObj: User = new User( '', nName, nEmail, nPassword, nRole, nContactNumber);

    console.log(UserObj);
    
    this.rest.register(UserObj).subscribe({
      next: (response: HttpResponse<any>) => {
        console.log(response.status);
        
        if (response.status == 200) {
          this.getDataFromServer();
          const closeBtn = document.getElementById('add-close-btn');
          if (closeBtn) {
            closeBtn.click();
          }
        }
      },
      error: (err) => {
        this.toastService.showError(err.error);
      },
      complete: () => {
        this.toastService.showSuccess('New User Account Created');
            this.getAgentsList();
        this.addUserForm.reset({
          nName: '',
          nEmail: '',
          nPassword: '',
          nContactNumber: '',
          rrole: '',
        });
      }
    })


  }

  // Edit User Details and Update
  editUserDetail() {
    let id = this.editProfileForm.get(['id'])?.value;
    let name = this.editProfileForm.get(['name'])?.value;
    let email = this.editProfileForm.get(['email'])?.value;
    let password = this.editProfileForm.get(['password'])?.value;
    let role = this.editProfileForm.get(['role'])?.value;
    let contactNumber = this.editProfileForm.get(['contactNumber'])?.value;

    let UserObj: User = new User(id, name, email, password, role, contactNumber);

    // Update the User Details
    // For DB.json
    // this.rest.updateData(UserObj).subscribe({
    //   next: (data) => {
    //     this.getDataFromServer();
    //     const closeBtn = document.getElementById('close-btn');
    //     if (closeBtn) {
    //       closeBtn.click();
    //     }
    //   },
    //   error: (err) => alert(JSON.stringify(err)),
    //   complete: () => {
    //     console.log('Update Data Successfull..')
    //   }
    // })

    this.rest.updateData(UserObj).subscribe({
      next: (response: HttpResponse<any>) => {

        if (response.status == 200) {
          this.getDataFromServer();
          const closeBtn = document.getElementById('close-btn');
          if (closeBtn) {
            closeBtn.click();
          }
        }
      },
      error: (err) => {
        this.toastService.showError(err.error);
      },
      complete: () => {
        this.toastService.showSuccess('User Details Updated');
      }
    })
  }

  // Getters for Form Controls
  get name() {
    return this.editProfileForm.get(['name']);
  }
  get email() {
    return this.editProfileForm.get(['email']);
  }
  get contactNumber() {
    return this.editProfileForm.get(['contactNumber']);
  }

  get nName(){
    return this.addUserForm.get(['nName']);
  }
  get nEmail(){
    return this.addUserForm.get(['nEmail']);
  }
  get nPassword(){
    return this.addUserForm.get(['nPassword']);
  }
  get nContactNumber(){
    return this.addUserForm.get(['nContactNumber']);
  }
  get nRole(){
    return this.addUserForm.get(['nRole']);
  }

  // For DB JSON
  // Fetch Data from Server
  // getDataFromServer() {
  //   this.rest.getData().subscribe({
  //     next: (data) => { this.userDetail = data, this.UserbyRoles = data },
  //     error: (err) => alert(JSON.stringify(err)),
  //     complete: () => console.log('Getting Details successfully')
  //   })
  // }

  getDataFromServer() {
    this.rest.getData().subscribe({
      next: (response: HttpResponse<any>) => {

        if (response.status == 200) {

          this.userDetail = response.body;
          this.UserbyRoles = response.body;
          // this.applyFilter(this.selectedRole);
        }
      },
      error: (err) => {
        this.toastService.showError(err.error);
        // alert("Unable to fetch data.")

      },
      complete: () => console.log("Data Fetched Successfully")
    })
  }

  // Delete User
  // For DB Json
  // deleteUser(id: string) {
  //   this.rest.deleteData(id).subscribe({
  //     next: (data) => { this.getDataFromServer() },
  //     error: (err) => alert(JSON.stringify(err)),
  //     complete: () => console.log('Getting Details successfully')
  //   })
  // }

  deleteUser(id: string) {
    this.rest.deleteData(id).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status == 200) {
          this.getDataFromServer();
        }
      },
      error: (err) =>{
        this.toastService.showError(err.error);
      },
      complete: () =>{
        this.toastService.showSuccess('User removed');
        console.log('Getting Details successfully')}
    })
  }

  // Custom Validator for Contact Number Length
  contactNumberLength(control: AbstractControl): ValidationErrors | null {
    const value = control.value?.toString();
    if (!value) return null;
    if (value.length !== 10) {
      return { invalidLength: true }
    }
    return null;
  }

  // Filter Users by Roles
  applyFilter(selectedRole: string) {
    this.selectedRole = selectedRole;
    if (selectedRole == "All") {
      this.getDataFromServer();
    }
    else {
      this.UserbyRoles = this.userDetail.filter(u => u.role === selectedRole);
    }
  }

  // Logout Admin
  logoutAdmin() {
    this.authentication.logout();
    this.route.navigate([""]);
  }

  // Search User by ID or Name
  searchFilter() {
    let filteredUsers = this.selectedRole === "All"
      ? this.userDetail
      : this.userDetail.filter(u => u.role === this.selectedRole);

    const idTerm = (this.userId || '').toLowerCase().trim();
    const nameTerm = (this.userName || '').toLowerCase().trim();
    if (idTerm !== '') {
      filteredUsers = filteredUsers.filter(u =>
        u.userID.toLowerCase().includes(idTerm)
      );
    }
    if (nameTerm !== '') {
      filteredUsers = filteredUsers.filter(u =>
        u.uname.toLowerCase().includes(nameTerm)
      );
    }
    this.UserbyRoles = filteredUsers;
  }


  // Toggle Dashboard and Support Ticket View
  changeDashboard() {
    this.dispDashboard = !this.dispDashboard;
    sessionStorage.setItem('dispDashboard', this.dispDashboard.toString());
    this.getSupportTicketData();
  }

  // Fetch Support Ticket Data
  // getSupportTicketData() {
  //   this.ticketService.getDataSupportTicketFromDB().subscribe({
  //     next: (data) => {
  //       this.SupportTicketData = data
  //         .map((ticket: any) => ({
  //           ...ticket,
  //           agentAssigned: ticket.status === 'Assigned' || ticket.status === 'Completed',
  //           completed: ticket.status === 'Completed'
  //         }));
  //     },
  //     error: (err) => alert(JSON.stringify(err)),
  //     complete: () => console.log("Get Data Operation Successfull...")
  //   })
  // }

  getSupportTicketData() {
    console.log("Inside function");

    this.ticketService.getAllSupportTickets().subscribe({
      next: (response: HttpResponse<any>) => {
        console.log("Data::");

        if (response.status == 200) {
          this.SupportTicketData = response.body.map((ticket: any) => ({
            ...ticket,
            agentAssigned: ticket.status === 'Assigned' || ticket.status === 'Completed',
            completed: ticket.status === 'Completed'
          }));

        }
      },
      error: (err) =>{
        this.toastService.showError(err.error);
      }
    });

  }

  // List of Agents
  agents: { userID: string, name: string }[] = [];

  getAgentsList() {
    this.ticketService.getAllAgents().subscribe({
      next: (data: { userID: string, name: string }[]) => {
        // Sort agents alphabetically by name
        this.agents = data.sort((a, b) => a.name.localeCompare(b.name));
      },
      error: (err) => console.error('Error fetching agents:', err)
    });
  }

  // Assign Agent to Ticket
  assignAgent(ticket: any) {
    if (ticket.agent) {
      this.ticketService.assignAgentToTicket(ticket.ticketId, ticket.agent).subscribe({
        next: () => {
          ticket.status = 'Assigned';
          ticket.agentAssigned = true;
        },
        error: err => {
          this.toastService.showError(err.error);
        }
      });
    }
  }

  // Complete Ticket by Agent 
  completeTicket(ticket: any) {
    this.ticketService.completeTicket(ticket.ticketId).subscribe({
      next: () => {
        ticket.status = 'Completed';
        ticket.completed = true;
      },
      error: err => {
        this.toastService.showError(err.error);
      }
    });
  }
}