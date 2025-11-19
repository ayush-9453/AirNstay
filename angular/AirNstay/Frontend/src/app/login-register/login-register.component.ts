import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { RegisterService } from '../register.service';
import User from '../User';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { getUserId } from '../Auth-Store/auth.action';
import { AuthenticationService } from '../authentication.service';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-login-register',
  standalone: false,
  templateUrl: './login-register.component.html',
  styleUrl: './login-register.component.css'
})
export class LoginRegisterComponent {

  // Toggle between login and registration form.
  bLoginRegister: boolean = true;
  // User list
  userLst!: User[];
  // Reactive Forms
  loginForm!: FormGroup;
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, private rest: RegisterService, private route: Router, private store: Store, private authentication: AuthenticationService,private toastService: ToastService) {
    // Login Form Validations
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: ['', Validators.required]
    })

    // Registration Form Validations
    this.registerForm = this.fb.group({
      ruserName: ['', Validators.required],
      remail: ['', [Validators.required, Validators.email]],
      rpassword: ['', [Validators.required, Validators.minLength(8)]],
      retypepassword: ['', [Validators.required, this.passwordValidator]],
      // rrole: ['', Validators.required]
    });
  }

  // Toggle between login and registration form.
  toggleLogin() {
    this.bLoginRegister = !this.bLoginRegister;
  }

  // Getters for Login Form
  get email() {
    return this.loginForm.get(['email']);
  }
  get password() {
    return this.loginForm.get(['password']);
  }
  get role() {
    return this.loginForm.get(['role']);
  }

  // Getters for Registration Form
  get ruserName() {
    return this.registerForm.get(['ruserName']);
  }
  get remail() {
    return this.registerForm.get(['remail']);
  }
  get rpassword() {
    return this.registerForm.get(['rpassword']);
  }
  get retypepassword() {
    return this.registerForm.get(['retypepassword']);
  }

  get passwordMismatch() {
    return this.registerForm.hasError('passwordMismatch');
  }

  // Login User Function 
  loginUser() {
    // Fetch form values 
    let email = this.loginForm.get(['email'])?.value.toLowerCase();
    let password = this.loginForm.get(['password'])?.value;
    let role = this.loginForm.get(['role'])?.value;
    let contactNumber = null;


    let userDetail : User = new User("","",email,password,role,contactNumber); 

    this.rest.login(userDetail).subscribe({
      next : (response : HttpResponse<any>) => {

        if(response.status == 200){

          const body = response.body;
          
          this.authentication.login(body.token,body.role,body.userID,body.email,body.contactNumber);
          
          // localStorage.setItem('token',body.token);
          // localStorage.setItem('userID',body.userID);
          // localStorage.setItem('email',body.email);
          // localStorage.setItem('role',body.role);
          
          const role = this.authentication.getRole();
          
          if (role === 'Admin') {
            this.route.navigate(['/adminDashboard']);
          }
          else if (role === 'Traveller') {
            this.route.navigate(['/flights']);
          }
          else if (role === 'Travel Agent') {
            this.route.navigate(['/travelAgent']);
          }
          else if (role === 'Support Agent') {
            this.route.navigate(['/supportDashboard']);
          }
        }
        else{
          console.log("Cannot Login");
        }
      },
      error : (err) => {
        this.toastService.showError(err.error);
      },
      complete: () => {
        this.toastService.showSuccess('Successfully Logged In!');
        this.loginForm.reset({
          email: '',
          password: '',
          role: ''
        });
      }
    })

    // For DB.Json
    // // Fetching user list from json server 
    // this.rest.getData().subscribe((users: User[]) => {
    //   const matchedUser = users.find((user) =>
    //     user.email === email &&
    //     user.password === password &&
    //     user.role === role
    //   );

    //   // If user found, navigate to respective dashboard based on role
    //   if (matchedUser) {
    //     sessionStorage.setItem('userId', matchedUser.userID);
    //     this.store.dispatch(getUserId({ userId: matchedUser.userID }));

    //     if (role === 'Admin') {
    //       this.authentication.login('Admin');
    //       this.route.navigate(['/adminDashboard']);
    //     }
    //     else if (role === 'Traveller') {
    //       this.authentication.login('Traveller');
    //       this.route.navigate(['/flights']);
    //     }
    //     else if (role === 'Travel Agent') {
    //       this.authentication.login('Travel Agent');
    //       this.route.navigate(['/travelAgent']);
    //     }
    //     else if (role === 'Hotel Manager') {
    //       this.authentication.login('Hotel Manager');
    //       alert('Hotel Manager')
    //     }
        
    //     this.loginForm.reset({
    //       email: '',
    //       password: '',
    //       role: ''
    //     });
    //   }
    //   // If user not found, show alert
    //   else {
    //     alert('Invalid Credentials')
    //   }
    // })
  }

  // Register User Function
  registerUser() {
    // Fetch form values
    let ruserName = this.registerForm.get(['ruserName'])?.value;
    let remail = this.registerForm.get(['remail'])?.value.toLowerCase();
    let rpassword = this.registerForm.get(['rpassword'])?.value;
    // let rrole = this.registerForm.get(['rrole'])?.value;
    let contactNumber = 0;

    // create new user
    let userDetail: User = new User("", ruserName, remail, rpassword, "Traveller", contactNumber);
    // Insert user details to json server
    this.rest.register(userDetail).subscribe({
      next: (response : HttpResponse<any>) => {
        if(response.status == 200){
          console.log("User Register Successfully");
        }
      },
      error: (err) => { 
        this.toastService.showError(err.error);
       },
      complete: () => {
        // Show success message and reset form
        this.toastService.showSuccess("User Registered Successfully");
        this.registerForm.reset({
          ruserName: '',
          remail: '',
          rpassword: '',
          retypepassword: '',
          rrole: '',
        });
        this.bLoginRegister = true;
      }
    })
  }

  // Custom validator to check if password and confirm password match
  passwordValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.parent) return null;

    const password = control.parent.get('rpassword')?.value;
    const confirmPassword = control.value;

    return password === confirmPassword ? null : { passwordMismatch: true };
  }
}
