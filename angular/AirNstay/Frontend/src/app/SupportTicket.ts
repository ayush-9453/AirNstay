export default class SupportTicket{
    ticketId! : string;
    userID : string | null;
    name! : string;
    detailedIssue! : string;
    date! : Date;
    status! : string;
    agent! : string;
    agentAssigned!: boolean;
    completed!: boolean;

    constructor(ticketId : string,userID :string | null, name : string, detailedIssue : string,date : Date)
    {
        this.ticketId=ticketId;
        this.userID=userID;
        this.name=name;
        this.detailedIssue=detailedIssue;
        this.date=date;
        this.status="Pending";
        this.agent="";
        this.agentAssigned = false;
        this.completed = false;
    }
}