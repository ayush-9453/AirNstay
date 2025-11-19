export default class HotelDetails {
    hotelImage1!: string; // changed from Blob to string
    hotelImage2!: string;
    hotelImage3!: string;
    hotelId!: string;
    name!: string;
    address!: string;
    city!: string;
    pincode!: string;
    landmark!: string;
    mapLink!: string;
    roomsAvailable!: number;
    pricePerNight: number;
    amenities!: string[];
    description!: string;
    earlyCheckIn!: string;
    freeBreakfast!: string;
    flexibleCancellation!: string;
    customerSupport!: string;
    loyaltyRewards!: string;

    constructor(
        hotelId: string, 
        name: string,
        address: string,
        city: string,
        pincode: string,
        landmark: string,
        mapLink: string,
        roomsAvailable: number, 
        pricePerNight: number, 
        amenities: string[], 
        description: string,
        earlyCheckIn: string,
        freeBreakfast: string,
        flexibleCancellation: string,
        customerSupport: string,
        loyaltyRewards: string
    ){
        this.hotelId = hotelId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.landmark = landmark;
        this.mapLink = mapLink;
        this.roomsAvailable = roomsAvailable;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.description = description;
        this.loyaltyRewards = loyaltyRewards;
        this.earlyCheckIn = earlyCheckIn;
        this.freeBreakfast = freeBreakfast;
        this.flexibleCancellation = flexibleCancellation;
        this.customerSupport = customerSupport;
    }
}