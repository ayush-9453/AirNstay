import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import Packages from './sub-package/Packages';
import Itinerary from './sub-package/Itinerary';

@Injectable({
  providedIn: 'root'
})
export class PackageServiceService {

  constructor(private packages : HttpClient) { }

  packageUrl = 'http://localhost:8090/packages'

  iternaryUrl = 'http://localhost:8090/itinerary'

  getAllPackages():Observable<any>{
    return this.packages.get(this.packageUrl+ "/allData");
  }

  
  insertPackages(pkg : Packages):Observable<any>{
    return this.packages.post(this.packageUrl + '/insert',pkg);
  }
  updatePackages(pkg : Packages):Observable<any>{
    const updatedUrl = this.packageUrl +'/update'
    return this.packages.put(updatedUrl,pkg)
  }
  deletePackages(id : number):Observable<any>{
    let deleteUrl = this.packageUrl + '/delete/' + id;
    return this.packages.delete(deleteUrl,{ responseType: 'text' });
  }
   getbylocation(location  : String): Observable<any>{
    return this.packages.get(this.packageUrl + "/" +location,{observe : 'response'})
  }
   
   getPackageById(id : number): Observable<any>{
      return this.packages.get(this.packageUrl + "/getPackagesById/"+ id , {observe: 'response'})
    }



  // itinerary services 
  getIternerayByid(id: number):Observable<any>{
    return this.packages.get(this.iternaryUrl+ "/" + id,{observe : 'response'})
  }
  insertItineraryByid(it : Itinerary):Observable<any>{
    return this.packages.post(this.iternaryUrl+"/insert",it)
  }
  updateItineraryByid(it: Itinerary):Observable<any>{
    let UpdateItineraryUrl = this.iternaryUrl +'/update/' + it.itineraryId;
    return this.packages.put(UpdateItineraryUrl,it)
  }
  deleteItineraryById(id:number):Observable<any>{
    let deleteItineraryUrl = this.iternaryUrl +"/delete/" +id;
    return this.packages.delete(deleteItineraryUrl, { responseType: 'text' });
  }
}