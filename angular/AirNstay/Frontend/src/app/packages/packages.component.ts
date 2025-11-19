import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-packages',
  standalone: false,
  templateUrl: './packages.component.html',
  styleUrl: './packages.component.css'
})
export class PackagesComponent {

  constructor(private route : Router){}

  searchlocation! : string;

  onsearch(){
    this.route.navigate(['/packages',this.searchlocation])
  }
  
  getPackageByName(location:string){
    this.route.navigate(['/packages',location.toLocaleLowerCase()])
  }

  locations = [
    { name: 'Goa',  image: '/package_img/pkg_img_1.jpg' },
    { name: 'Germany',  image: '/package_img/pkg_img_2.jpg' },
    { name: 'Rajasthan',  image: '/package_img/pkg_img_3.jpg' },
    { name: 'Greece',  image: "/package_img/pkg_img_4.jpg" },
    { name: 'Dubai', image: '/package_img/pkg_img_5.jpg' },
    { name: 'Paris', image: '/package_img/pkg_img_6.jpg' },
  ];


}
