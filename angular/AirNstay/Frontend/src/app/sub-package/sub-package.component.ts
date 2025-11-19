import { Component } from '@angular/core';
import { PackageServiceService } from '../package-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import Packages from './Packages';
import Itinerary from './Itinerary';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-sub-package',
  standalone: false,
  templateUrl: './sub-package.component.html',
  styleUrl: './sub-package.component.css',
})
export class SubPackageComponent {
  location: string="";
  packages: Packages[] = [];
  bdisplayPackageDetails: boolean = false;
  selectedPackage: Packages | null = null;
  itiernary: Itinerary | null = null;
  carouselImages: string[] = [];
  startlocation: string = 'New Delhi';
  endlocation: string='';
  startingDate: string ="";
  minDate!: string ;

  constructor(
    private packagesdata: PackageServiceService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService : ToastService,
  ) {
    this.startingDate = new Date().toISOString().split('T')[0];
    this.minDate = this.startingDate;
  }

  private fetchPackages(location: string): void {
    this.packagesdata.getbylocation(location.toLowerCase()).subscribe({
        next: (response: HttpResponse<any>) => {
          if (response.status === 202 && response.body) {
            this.packages = response.body;
            this.carouselImages = this.packages.map((pkg) => pkg.imageUrl);
            this.endlocation = location; 
          } else {
            console.error('Backend status error or empty body:', response.status);
            this.packages = []; 
            this.carouselImages = [];
          }
        },
        error: (err) => {
          this.toastService.showError(err.error)
          console.error('Error fetching packages:', err);
          this.packages = [];
          this.carouselImages = [];
        },
      });
  }

  
  ngOnInit(): void {
    this.location = this.route.snapshot.paramMap.get('place') || '';
    if (this.location) {
      this.fetchPackages(this.location);
    }
  }

  onSearch() {
    if (this.endlocation) {
      this.fetchPackages(this.endlocation);
    }
  }


  bookPackage(pkg: Packages) {
    this.bdisplayPackageDetails = true;
    this.selectedPackage = pkg;

    this.packagesdata.getIternerayByid(pkg.id).subscribe({

      next: (response: HttpResponse<any>) => {
        if (response.status == 202) {
          this.itiernary = response.body;
          console.log('itinerary: ', this.itiernary);
        } else {
          console.log(response.status)
          console.log('backend status error bookpackages');
        }
      },
      error: (err) => this.toastService.showError(err.error),
    });
  }

  getDateFromStarting(index: number) {
    const start = new Date(this.startingDate);
    start.setDate(start.getDate() + index);
    return start;
  }

  // payment gateway
  gotopaymentpage() {
    console.log(this.selectedPackage);
    if (!this.selectedPackage) {
      console.warn('No package selected, cannot navigate to payment page.');
      return;
    }
    this.router.navigate(['/packages-Payment'], {
      queryParams: { id: this.selectedPackage.id },
    });
  }

  //navigiate to user profile
  onsupport() {
    this.router.navigate(['./userProfile']);
  }
}
