export default class FlightDetails {
    flightLogo! : string;
    flightId!: string;
    airline!: string;
    departure!: string;
    arrival!: string;
    departureDate!: string;
    arrivalDate!: string;
    price!: number;
    availability!: string;
    departureTime!: string;
    arrivalTime!: string;
    duration! : string;
    classType! : string;

    constructor(flightLogo : string, flightId: string, airline: string, departure: string, arrival: string, departureDate: string, arrivalDate: string, price: number, availability: string , departureTime: string, arrivalTime: string , duration : string , classType : string) {
        this.flightLogo = flightLogo;
        this.flightId = flightId;
        this.airline = airline;
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.price = price;
        this.availability = availability;
        this.departureTime = departureTime;
        this.duration = duration;
        this.arrivalTime = arrivalTime;
        this.classType = classType;
    }
}





