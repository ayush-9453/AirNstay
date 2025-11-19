import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  toasts: any[] = [];

  showError(message: string) {
    const toast = {
      message: message,
      classname: 'bg-danger text-light', 
      delay: 2000 
    };

    this.toasts.push(toast);

    setTimeout(() => this.remove(toast), toast.delay);
  }

  showSuccess(message: string) {
    const toast = {
      message: message,
      classname: 'bg-success text-light', 
      delay: 2000
    };
    
    this.toasts.push(toast);
    setTimeout(() => this.remove(toast), toast.delay);
  }

  remove(toast: any) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }
}