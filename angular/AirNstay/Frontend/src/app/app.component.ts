import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { getUserId } from './Auth-Store/auth.action';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'

})
export class AppComponent {
  title = 'airNstay';

  constructor(private store: Store) { }

  ngOnInit() {
    const userId = sessionStorage.getItem('userId');
    if (userId) {
      this.store.dispatch(getUserId({ userId }));
    }
  }





}
