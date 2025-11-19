import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import User from './User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private rest: HttpClient) { }

  // strUrl: string = "http://localhost:3000/User";
  strUrl: string = "http://localhost:8090";

  // CRUD Operations
  // Create
  register(userObj: User): Observable<any> {
    return this.rest.post(this.strUrl + "/loginRegister/register", userObj, { observe: 'response' });
  }

  login(userObj : User):Observable<any> {
    return this.rest.post(this.strUrl + "/loginRegister/login",userObj ,{observe:'response'});
  }

  insertData(userObj: User): Observable<any> {
    return this.rest.post(this.strUrl + "/loginRegister/", userObj, { observe: 'response' });
  }
  // Read
  getData(): Observable<any> {
    return this.rest.get(this.strUrl + "/adminDashboard", { observe: 'response' });
  }

  // Update
  updateData(UserObj: User): Observable<any> {
    let strUpdateURL = this.strUrl + '/adminDashboard/' + UserObj.userID;
    return this.rest.put(strUpdateURL, UserObj, { observe: 'response' });
  }

  // Delete
  deleteData(id: string): Observable<any> {
    let deleteRecordUrl = this.strUrl + '/adminDashboard/' + id;
    return this.rest.delete(deleteRecordUrl, { responseType: 'text', observe: 'response' });
  }
}
