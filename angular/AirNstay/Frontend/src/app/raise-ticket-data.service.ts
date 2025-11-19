import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import SupportTicket from './SupportTicket';
import { filter, map, Observable } from 'rxjs';
import AllReviews from './AllReviews';
import ConstValues from './ConstValues';
import { tick } from '@angular/core/testing';

@Injectable({
  providedIn: 'root'
})
export class RaiseTicketDataService {

  constructor(private obj: HttpClient) { }

  url: string = "http://localhost:8090/support";

  AddTickettoDB(ticket: SupportTicket): Observable<any> {
  return this.obj.post(this.url + "/raise", ticket, { observe: 'response' });
}


getTicketById(userId : string):Observable<SupportTicket[]>{
  return this.obj.get<SupportTicket[]>(this.url + '/getMyTickets/'+ userId)
}
  surl: string = "http://localhost:3000/AllReviews";
  AddReviewtoDB(newReview: AllReviews): Observable<any> {
    return this.obj.post(this.surl, newReview);
  }

  getDataFromDB(): Observable<AllReviews[]> {
    return this.obj.get<AllReviews[]>(this.surl);
  }
  getDataByID(hotelId: string | null): Observable<AllReviews[]> {
    return this.getDataFromDB().pipe(
      map(review => review.filter(review => review.hotelId === hotelId))
    )
  }

  // getDataSupportTicketFromDB(): Observable<any> {
  //   return this.obj.get(this.url);
  // }

  // assignAgentToTicket(id: string, agent: string): Observable<any> {
  //   return this.obj.patch(`${this.url}/${id}`, {
  //     agent: agent,
  //     status: 'Assigned',
  //     agentAssigned: true
  //   });
  // }

  // completeTicket(id: string): Observable<any> {
  //   return this.obj.patch(`${this.url}/${id}`, {
  //     status: 'Completed',
  //     completed: true
  //   });
  // }

  adminUrl: string = "http://localhost:8090/adminDashboard";
  
  getAllSupportTickets(): Observable<any> {
    return this.obj.get(`${this.adminUrl}/supportTickets`,{ observe: 'response' });
  }

  supportUrl: string = "http://localhost:8090/supportAgent";
  getTicketsByAgentId(agentId: string): Observable<any> {
        return this.obj.get(`${this.supportUrl}/tickets/${agentId}`, { observe: 'response' });
  }


  // Assign agent to ticket
  assignAgentToTicket(id: string, agent: string): Observable<any> {
    return this.obj.patch(`${this.adminUrl}/supportTickets/assign/${id}`, { agent },{ observe: 'response' });
  }

  // Complete ticket
  completeTicket(id: string): Observable<any> {
    return this.obj.patch(`${this.supportUrl}/supportTickets/complete/${id}`, {},{ observe: 'response' });
  }

  getAllAgents(): Observable<any> {
  return this.obj.get(`${this.adminUrl}/agents`);
  }


  

  // UserProfileURL: string = "http://localhost:3000/User";
  UserProfileURL : string = "http://localhost:8090/User";

  getUserProfileDataFromDB(userId:string|null): Observable<any> {
    return this.obj.get(`${this.UserProfileURL}/${userId}`,{observe:'response'});
  }


  updateUserData(userId: string | null, updatedData: any): Observable<any> {
    console.log(userId,updatedData)
    return this.obj.patch(`${this.UserProfileURL}/${userId}`, updatedData);
  }

  updatepassword(userID: string | null, newpassword: string): Observable<any> {
    let userURl = this.UserProfileURL + "/" + userID;
    return this.obj.patch(userURl, { password: newpassword });
  }
}
