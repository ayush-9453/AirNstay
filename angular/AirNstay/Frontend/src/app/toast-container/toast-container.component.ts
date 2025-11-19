import { Component } from '@angular/core';
import { ToastService } from '../toast.service'; // Adjust path as needed

@Component({
  selector: 'app-toast-container',
  standalone: false,
  templateUrl: './toast-container.component.html',
  styleUrls: ['./toast-container.component.css']
})
export class ToastContainerComponent {
  bSuccess: boolean = true;
  bError: boolean = false;
  // Inject the service. 
  // The HTML template will now have access to it.
  constructor(public toastService: ToastService) { }
}