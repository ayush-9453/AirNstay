import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'fAQFilter',
  standalone: false
})
export class FAQFilterPipe implements PipeTransform {

  transform(FAQs : any[], searchQuery : string): any[] {
    if(!FAQs) return [];

    searchQuery = searchQuery.toLowerCase();
     return FAQs.filter(f =>
      f.question.toLowerCase().includes(searchQuery) ||
      f.answer.toLowerCase().includes(searchQuery) 
    );


  }

}
