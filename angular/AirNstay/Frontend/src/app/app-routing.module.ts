import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { LoginRegisterComponent } from './login-register/login-register.component';
import { FlightsComponent } from './flights/flights.component';
import { SearchResultsComponent } from './search-results/search-results.component';
import { PackagesComponent } from './packages/packages.component';
import { SubPackageComponent } from './sub-package/sub-package.component';
import { TravelAgentComponent } from './travel-agent/travel-agent.component';
import { HotelComponent } from './hotel/hotel.component';
import { HotelDetailsComponent } from './hotel-details/hotel-details.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { FlightPaymentComponent } from './flight-payment/flight-payment.component';
import { HotelPaymentComponent } from './hotel-payment/hotel-payment.component';
import { PackagePaymentComponent } from './package-payment/package-payment.component';
import { PaymentSuccessComponent } from './payment-success/payment-success.component';
import { authGuard } from './auth.guard';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { SupportDashboardComponent } from './support-dashboard/support-dashboard.component';



const routes: Routes = [
  { path: '', redirectTo: '/LoginRegister', pathMatch: 'full' },
  { path: 'LoginRegister', component: LoginRegisterComponent },
  { path: 'adminDashboard', component: AdminDashboardComponent, canActivate: [authGuard], data: { role: 'Admin' } },
  { path: 'supportDashboard',component:SupportDashboardComponent, canActivate: [authGuard], data: { role: 'Support Agent' }},
  { path: 'flights', component: FlightsComponent },
  { path: 'flights/search', component: SearchResultsComponent },
  { path: 'packages', component: PackagesComponent },
  { path: 'packages/:place', component: SubPackageComponent },
  { path: 'travelAgent', component: TravelAgentComponent, canActivate: [authGuard], data: { role: 'Travel Agent' } },
  { path: 'hotel', component: HotelComponent },
  { path: 'hotel-results', component: HotelDetailsComponent },
  { path: 'userProfile', component: UserProfileComponent, canActivate:[authGuard], data: {role:'Traveller'}},
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: 'hotel-results', component: HotelDetailsComponent },
  { path: 'flight-Payment', component: FlightPaymentComponent , canActivate: [authGuard], data: { role: 'Traveller' } },
  { path: 'hotel-Payment', component: HotelPaymentComponent, canActivate: [authGuard], data: { role: 'Traveller' }},
  { path: 'packages-Payment', component: PackagePaymentComponent, canActivate: [authGuard], data: { role: 'Traveller' } },
  { path: 'payment-success', component: PaymentSuccessComponent, canActivate: [authGuard], data: { role: 'Traveller' } },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    anchorScrolling: 'enabled',
    scrollOffset: [0, 64] // optional: offset for fixed headers
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
