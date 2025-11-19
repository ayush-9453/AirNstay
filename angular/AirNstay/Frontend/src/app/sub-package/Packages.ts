export default class Packages {
  id! : number;
  location!: string;
  title!: string;
  price!: number;
  duration!: string;
  imageUrl!: string;
  inclusions!: string;
  features!: any[];
  AgentId! : string |null;

  constructor(id :number, location: string,title: string,price: number,duration: string,imageUrl: string,inclusions: string,features: any[],AgentId:string | null ) {

    this.id = id
    this.location = location;
    this.title = title;
    this.price = price;
    this.duration = duration;
    this.imageUrl = imageUrl;
    this.inclusions = inclusions;
    this.features = features;
    this.AgentId = AgentId;
   
  }

}