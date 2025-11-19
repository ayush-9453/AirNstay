import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private route: Router,private toast : ToastService) { }

  login(token: string, role: string, userID: string, email: string , contactNumber : Number) {
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('userID', userID);
    localStorage.setItem('email', email);
    localStorage.setItem('contact', String(contactNumber));

  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUserID(): string | null {
    return localStorage.getItem('userID');
  }

  getEmail(): string | null {
    return localStorage.getItem('email');
  }

  getContact() : string | null {
     return localStorage.getItem('contact');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout() {
    localStorage.clear();
    this.toast.showSuccess("Logged out successfully");
    setTimeout(()=>{this.route.navigate(['/'])},100);;
  }

}
