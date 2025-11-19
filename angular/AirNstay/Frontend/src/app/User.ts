export default class User {
    userID!: string;
    uname!: string;
    email!: string;
    password!: string;
    role!: string;
    contactNumber!: number | null;
    gender!: string;

    constructor(id: string, name: string, email: string, password: string, role: string, contactNumber: number | null) {
        this.userID = id;
        this.uname = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.gender = "";
    }

}
