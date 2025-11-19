export default class PackageBookingDetails {
  // Fields present in the original POJO
  location: string;
  packageTitle: string;
  price: number; // Note: 'amount' from payment is used here, or you might keep both
  duration: string;
  agentId: string;
  // id: string; // The backend data doesn't explicitly have an 'id' at the top level
  bookingId: string;
  userId: string;
  paymentId: string; // Extracted from the nested 'payment' object
  status: string;
  type: string;

  // New fields from the backend data
  bookingDate: string;
  travellers: Traveller[]; // Array of Traveller objects
  contactInfo: ContactInfo; // ContactInfo object
  payment: Payment; // Payment object

  constructor(
    location: string,
    packageTitle: string,
    price: number, // Using price for the amount from payment
    duration: string,
    agentId: string,
    bookingId: string,
    userId: string,
    paymentId: string,
    bookingDate: string,
    travellers: Traveller[],
    contactInfo: ContactInfo,
    payment: Payment,
    status: string = 'confirmed',
    type: string = 'packages',
  ) {
    this.location = location;
    this.packageTitle = packageTitle;
    this.price = price;
    this.duration = duration;
    this.agentId = agentId;
    this.bookingId = bookingId;
    this.userId = userId;
    this.paymentId = paymentId;
    this.bookingDate = bookingDate;
    this.travellers = travellers;
    this.contactInfo = contactInfo;
    this.payment = payment;
    this.status = status;
    this.type = type;
  }
}

// --- Interface/Type Definitions for Nested Objects ---

export interface Traveller {
  travellerId: number;
  firstName: string;
  lastName: string;
  age: number;
  gender: string;
}

export interface ContactInfo {
  contactId: number;
  country: string;
  phone: string;
  email: string;
}

export interface Payment {
  paymentId: string;
  userId: string;
  amount: number;
  status: string;
  method: string;
  timestamp: string;
}