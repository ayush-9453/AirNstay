import { Component } from '@angular/core';
import Packages from '../sub-package/Packages';
import { PackageServiceService } from '../package-service.service';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import Itinerary from '../sub-package/Itinerary';
import { AuthenticationService } from '../authentication.service';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { storeUserId } from '../Auth-Store/auth.selector';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-travel-agent',
  standalone: false,
  templateUrl: './travel-agent.component.html',
  styleUrl: './travel-agent.component.css',
})
export class TravelAgentComponent {
  addPackagesForm!: FormGroup;
  packagesdata: Packages[] = [];
  searchLocation: string = '';
  featuresList: string[] = [
    'Flight',
    'Hotel',
    'Meals',
    'Sightseeing',
    'Transfers',
    'City Tour',
  ];

  userId!: string | null;
  selectedPackageId!: number;
  itineraryDuration!: string;
  itineraryForm!: FormGroup;
  itineraryData!: Itinerary;

  constructor(private packageservice: PackageServiceService,
    private fb: FormBuilder,private authentication: AuthenticationService,
    private route: Router,private toastService : ToastService
  ) {
    // AddPackageForm defined
    this.addPackagesForm = this.fb.group({
      id: [0],
      title: ['', [Validators.required, Validators.pattern('[a-zA-Z\\s]+')]],
      duration: ['', Validators.required],
      location: ['', [Validators.required, Validators.pattern('[a-zA-Zs]+')]],
      price: ['', Validators.required],
      imageUrl: ['', Validators.required],
      inclusions: ['', Validators.required],
      features: this.fb.array([], Validators.required),
    });

    // ItineraryForm defined
    this.itineraryForm = this.fb.group({
      packageId: ['', Validators.required],
      imagesUrls: this.fb.array([this.fb.control('')]),
      DayItinerary: this.fb.array([]),
    });
  }

  // ng oninit
  ngOnInit(): void {
    this.userId = localStorage.getItem('userID');
    this.packageservice.getAllPackages().subscribe({
      next: (data) => {
        this.packagesdata =  data.filter((pkg: Packages) => pkg.AgentId == this.userId)
        console.log(this.packagesdata);
      },
      error: (err) => this.toastService.showError(err.error),
    });
  }

  loadPackages(): void {
    this.packageservice.getAllPackages().subscribe({
      next: (data) => {
        this.packagesdata = data.filter((pkg: Packages) => pkg.AgentId === this.userId);
        console.log('Packages loaded:', this.packagesdata);
      },
      error: (err) => console.error('Error loading packages:', err),
    });
  }

  // on search filter
  onSearch(): void {
    const query = this.searchLocation.trim().toLowerCase();
    this.packageservice.getAllPackages().subscribe({
      next: (data) => {
        let filteredData = data.filter((pkg: Packages) => pkg.AgentId === this.userId);
        if (query) {
          this.packagesdata = filteredData.filter((pkg: Packages) =>
            pkg.location.toLowerCase().startsWith(query)
          );
        } else {
          this.packagesdata = filteredData;
        }
      },
      error: (err) => {
        this.toastService.showError(err.error)
        console.error('Error in onSearch function is:', err)
      },
    });
  }

  // get methods for addPackageForm
  get id() {
    return this.addPackagesForm.get('id');
  }
  get title() {
    return this.addPackagesForm.get('title');
  }
  get duration() {
    return this.addPackagesForm.get('duration');
  }
  get features() {
    return this.addPackagesForm.get('features') as FormArray;
  }
  get location() {
    return this.addPackagesForm.get('location');
  }
  get price() {
    return this.addPackagesForm.get('price');
  }

  // get method for itinernaryForm
  get packageId() {
    return this.itineraryForm.get('packageId');
  }
  get ItinerarysItinerary(): FormArray {
    return this.itineraryForm.get('DayItinerary') as FormArray;
  }
  get imagesUrls(): FormArray {
    return this.itineraryForm.get('imagesUrls') as FormArray;
  }

  // Add and remove image in form of array
  addImage() {
    this.imagesUrls.push(this.fb.control(''));
  }

  removeImage(index: number) {
    this.imagesUrls.removeAt(index);
  }

  onModalClose() {
    this.addPackagesForm.reset({ id: 0 });
    this.features.clear();
    this.itineraryForm.reset();
    this.imagesUrls.clear();
    this.ItinerarysItinerary.clear();
  }

  //Add and remove Itinerary form the modal
  addItinerary() {
    let nextDay = this.ItinerarysItinerary.length + 1;
    const itineraryGroup = this.fb.group({
      day: [nextDay, Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      mealsIncluded: ['', Validators.required],
      overnightStay: ['', Validators.required],
      highlights: ['', Validators.required],
    });
    this.ItinerarysItinerary.push(itineraryGroup);
  }

  removeItinerary(index: number) {
    this.ItinerarysItinerary.removeAt(index);
  }

  // created a checkbox in modal to add feature in packages
  onCheckboxChange(event: any): void {
    const featuresArray = this.features;
    if (event.target.checked) {
      featuresArray.push(this.fb.control(event.target.value));
    } else {
      const index = featuresArray.controls.findIndex(
        (x) => x.value === event.target.value
      );
      if (index >= 0) {
        featuresArray.removeAt(index);
      }
    }
  }

  // edit button that fetch values from backend and put it in form
  editPackageRecord(pkg: Packages): void {
    this.features.clear();
    this.addPackagesForm.patchValue({
      id: pkg.id,
      title: pkg.title,
      duration: pkg.duration.split('/')[0].replace('D', ''),
      location: pkg.location,
      price: pkg.price,
      imageUrl: pkg.imageUrl,
      inclusions: pkg.inclusions,
    });

    pkg.features.forEach((feature: string) => {
      this.features.push(this.fb.control(feature));
    });
    console.log('Started Editing:', this.addPackagesForm.value);
  }

  // Edit and update packages button in modal
  editPackage(): void {
    let id = this.addPackagesForm.get(['id'])?.value;
    let title = this.addPackagesForm.get(['title'])?.value;
    let duration = this.addPackagesForm.get(['duration'])?.value;
    let location = this.addPackagesForm.get(['location'])?.value;
    let price = this.addPackagesForm.get(['price'])?.value;
    let imageUrl = this.addPackagesForm.get(['imageUrl'])?.value;
    let inclusions = this.addPackagesForm.get(['inclusions'])?.value;
    let features = this.addPackagesForm.get(['features'])?.value;

    let durations = duration + 'D/' + (duration - 1) + 'N';

    let PackageObj: Packages = new Packages(
      id,
      location,
      title,
      price,
      durations,
      imageUrl,
      inclusions,
      features,
      this.userId // to do add( Travel manager id)
    );

    this.packageservice.updatePackages(PackageObj).subscribe({
      next: (reponse: HttpResponse<any>) => {
        if (reponse.status == 202) {
          this.loadPackages();
          this.onModalClose();
          window.location.reload();
        }
      },
      error: (err) => {
        this.toastService.showError(err.error)
        console.log("Error updateing packages:", JSON.stringify(err))
      },
      complete: () => console.log('Update Data Successful..'),
    });
    // this.addPackagesForm.reset();
  }

  // deletepackages using id and itinerary also deletes with same id
  deletePackage(id: number): void {
    console.log('Deleting package with ID:', id);
    this.packageservice.deletePackages(id).subscribe({
      next: (res) => {
        console.log("message:",res)
        this.loadPackages();
        window.location.reload();
      },
      error: (err) => console.log("Error deleting packages:", JSON.stringify(err)),
      complete: () => console.log('Packages deleted successfully!'),
    });
    this.packageservice.deleteItineraryById(id).subscribe({
      next: () => console.log('Iternary removed by id ', id),
    });
  }

  // Add package

  onAddPackage(): void {
    let duration = this.duration?.value;
    let durations = duration + 'D/' + (duration - 1) + 'N';

    const newPackage = new Packages(
      0,
      this.location?.value,
      this.title?.value,
      this.price?.value,
      durations,
      this.addPackagesForm.get('imageUrl')?.value,
      this.addPackagesForm.get('inclusions')?.value,
      this.features?.value,
      this.userId
    );
    console.log("Adding new packages: ", newPackage);
    this.packageservice.insertPackages(newPackage).subscribe({
      next: () => {
        this.loadPackages();
        this.onModalClose();
        window.location.reload();
      },
      error: (err) => this.toastService.showError(err.error),
      complete: () => console.log('Package added successfully'),
    });

    // this.addPackagesForm.reset();
  }

  // button to open itinerary modal and patch value to form
  openItineraryModal(pkg: any): void {
    this.selectedPackageId = pkg.id;
    this.itineraryForm.patchValue({
      packageId: this.selectedPackageId,
    });
    this.packageservice.getIternerayByid(this.selectedPackageId).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status == 202) {
          let itineraryData : Itinerary = response.body
          if (itineraryData && itineraryData.imagesUrls && itineraryData.dayItinerary) {
            // Patch image URLs
            itineraryData.imagesUrls.forEach(url => {
              this.imagesUrls.push(this.fb.control(url));
            });

            // Patch day itinerary
            itineraryData.dayItinerary.forEach(dayItem => {
              this.ItinerarysItinerary.push(this.fb.group(dayItem));
            });
          } else {
            // If no data, ensure at least one image/day form is ready for creation
            this.addImage();
            this.addItinerary();
          }
        }
      },
      error :(err) => {
        console.warn("No existing itinerary found");
        this.addImage();
        this.addItinerary();
      }
    })
    console.log("Itinerary from packages : ", this.itineraryForm.value);
  }

  // on add itinerary button in modal
  onItinerarySubmit() {
    const newItinerary = new Itinerary(
      this.packageId?.value,
      this.imagesUrls?.value,
      this.ItinerarysItinerary?.value
    );
    console.log('itinerary are : ', newItinerary);
    this.packageservice.insertItineraryByid(newItinerary).subscribe({
      next: () => {
        console.log('Itinerary Added');
        this.onModalClose();
      },
      error: (err) => console.log("Error saving itinerary!" + JSON.stringify(err)),
    });
  }

  logoutTravelAgent() {
    this.authentication.logout();
    this.route.navigate(['']);
  }
}
