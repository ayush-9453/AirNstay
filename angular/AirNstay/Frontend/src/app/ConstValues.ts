export default class ConstValues{
    id: number = 1;
    ticketId! : number;

    constructor(tickeId : number)
    {
        this.ticketId=tickeId;
    }
}