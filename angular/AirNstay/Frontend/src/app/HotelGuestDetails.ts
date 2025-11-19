export default class HotelGuestDetails{
    firstName!:string;
    lastName!:string;
    gender!:string;
    age!:number;
    id!:string;
    
    constructor( firstName:string, lastName:string,gender:string,age:number,id:string){
        this.firstName=firstName;
        this.lastName=lastName;
        this.gender=gender;
        this.age=age;
        this.id=id;
    }
}
