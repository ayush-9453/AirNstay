
export default class FlightBookingDetails {
  bookingId!: string;
  userId!: string;
  type!: string;
  status!: string;
  bookingDate!: string;

  airline!: string;
  flightId!: string;
  departure!: string;
  arrival!: string;
  departureDate!: string;
  arrivalDate!: string;
  duration!: string;
  classType!: string;

  travellers!: Traveller[];
  contactInfo!: ContactInfo;
  payment!: Payment;

  constructor(data: any) {
    this.bookingId = data.bookingId;
    this.userId = data.userId;
    this.type = data.type;
    this.status = data.status;
    this.bookingDate = data.bookingDate;

    this.airline = data.airline;
    this.flightId = data.flightId;
    this.departure = data.departure;
    this.arrival = data.arrival;
    this.departureDate = data.departureDate;
    this.arrivalDate = data.arrivalDate;
    this.duration = data.duration;
    this.classType = data.classType;

    this.travellers = data.travellers.map((t: any) => new Traveller(t));
    this.contactInfo = new ContactInfo(data.contactInfo);
    this.payment = new Payment(data.payment);
  }
}

class Traveller {
  travellerId!: number;
  firstName!: string;
  lastName!: string;
  age!: number;
  gender!: string;

  constructor(data: any) {
    this.travellerId = data.travellerId;
    this.firstName = data.firstName;
    this.lastName = data.lastName;
    this.age = data.age;
    this.gender = data.gender;
  }
}

class ContactInfo {
  contactId!: number;
  country!: string;
  phone!: string;
  email!: string;

  constructor(data: any) {
    this.contactId = data.contactId;
    this.country = data.country;
    this.phone = data.phone;
    this.email = data.email;
  }
}

class Payment {
  paymentId!: string;
  userId!: string;
  amount!: number;
  status!: string;
  method!: string;
  timestamp!: string;

  constructor(data: any) {
    this.paymentId = data.paymentId;
    this.userId = data.userId;
    this.amount = data.amount;
    this.status = data.status;
    this.method = data.method;
    this.timestamp = data.timestamp;
  }
}
