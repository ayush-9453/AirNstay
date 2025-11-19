export default class Itinerary {
  itineraryId: number;
  imagesUrls: string[];
  dayItinerary: {
    day: number;
    title: string;
    description: string;
    mealsIncluded: string;
    overnightStay: string;
    highlights: string;
  }[];

  constructor(
    itineraryId: number,
    imagesUrls: string[],
    dayItinerary: {
      day: number;
      title: string;
      description: string;
      mealsIncluded: string;
      overnightStay: string;
      highlights: string;
    }[]
  ) {
    this.itineraryId = itineraryId;
    this.imagesUrls = imagesUrls;
    this.dayItinerary = dayItinerary;
  }
}