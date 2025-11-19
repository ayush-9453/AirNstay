export default class UserProfileDetails{
    UserID! : string;
    Name! : string;
    Email! : string;
    Mobile! : string;
    Gender! : string;
    Birth_Date! : string;
    Nationality! : string;
    City! : string;
    State! : string;

    constructor(UserID : string,Name : string, Email : string, Mobile : string, Gender : string, Birth_Date : string, Nationality : string, City : string, State : string)
    {
        this.UserID=UserID;
        this.Name=Name;
        this.Email=Email;
        this.Mobile=Mobile;
        this.Gender=Gender;
        this.Birth_Date=Birth_Date;
        this.Nationality=Nationality;
        this.City=City;
        this.State=State;
    }
}