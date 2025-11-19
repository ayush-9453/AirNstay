import { Observable } from "rxjs";


export default class AllReviews{
    reviewId! : string;
    hotelId! : string;
    rating! : string;
    name! : string;
    comment! : string;
    timestamp! : string;
    userId! : string;

    constructor(reviewId: string ,  rating : string,name : string,comment : string, timestamp : string, userId$ : string,hotelId : string)
    {
        this.reviewId=reviewId;
        this.rating=rating;
        this.userId=userId$;
        this.name=name;
        this.comment=comment;
        this.timestamp = timestamp;
        
        this.hotelId = hotelId;
    }

}