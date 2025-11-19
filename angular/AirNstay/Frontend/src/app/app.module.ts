import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginRegisterComponent } from './login-register/login-register.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { FAQFilterPipe } from './faq-filter.pipe';
import { PackagesComponent } from './packages/packages.component';
import { FooterComponent } from './footer/footer.component';
import { SubPackageComponent } from './sub-package/sub-package.component';
import { FlightsComponent } from './flights/flights.component';
import { SearchResultsComponent } from './search-results/search-results.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { TravelAgentComponent } from './travel-agent/travel-agent.component';
import { HotelComponent } from './hotel/hotel.component';
import { HotelDetailsComponent } from './hotel-details/hotel-details.component';
import { StoreModule } from '@ngrx/store';
import { authProfileReducer } from './Auth-Store/auth.reducer';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlightPaymentComponent } from './flight-payment/flight-payment.component';
import { HotelPaymentComponent } from './hotel-payment/hotel-payment.component';
import { CurrencyConverterPipe } from './currency-converter.pipe';
import { PackagePaymentComponent } from './package-payment/package-payment.component';
import { PaymentSuccessComponent } from './payment-success/payment-success.component';
import { AuthInterceptor } from './auth-interceptor.service';
import SupportTicket from './SupportTicket';
import { SupportDashboardComponent } from './support-dashboard/support-dashboard.component';
import { ToastContainerComponent } from './toast-container/toast-container.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginRegisterComponent,
    AdminDashboardComponent,
    FAQFilterPipe,
    PackagesComponent,
    FooterComponent,
    SubPackageComponent,
    FlightsComponent,
    SearchResultsComponent,
    NavbarComponent,
    TravelAgentComponent,
    HotelComponent,
    HotelDetailsComponent,
    UserProfileComponent,
    UnauthorizedComponent,
    UserProfileComponent,
    FlightPaymentComponent,
    HotelPaymentComponent,
    CurrencyConverterPipe,
    PackagePaymentComponent,
    PaymentSuccessComponent,
    SupportDashboardComponent,
    ToastContainerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    StoreModule.forRoot({ userId: authProfileReducer }),
  ],
  providers:
    [
      { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
    ]
  ,
  bootstrap: [AppComponent]
})
export class AppModule { }
