import { Component } from '@angular/core';

import { Store } from '@ngrx/store';
import { storeUserId } from '../Auth-Store/auth.selector';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  isLoggedIn : boolean = false;
  userId! : string | null;

  constructor(private store : Store) {}

  ngOnInit(){

    this.userId = localStorage.getItem('userID');

    console.log("Navbar userId: "+this.userId);

    if(this.userId != null){
      this.isLoggedIn = true;
    }
    else{
      this.isLoggedIn = false;
    }
  
  }
}
