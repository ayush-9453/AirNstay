export default class TravellerDetails{
    firstName!:string;
    lastName!:string;
    gender!:string;
    age!:number;
    id!:string;
    type!:string;
    
    constructor( firstName:string,
    lastName:string,
    gender:string,
    age:number,id:string,type:string){
        this.firstName=firstName;
        this.lastName=lastName;
        this.gender=gender;
        this.age=age;
        this.id=id;
        this.type=type;
    }
   
}