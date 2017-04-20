import { Pipe, PipeTransform } from '@angular/core';

import { Category } from '../objects/category';

@Pipe({ name: 'categoryFilter' })
export class CategoryFilterPipe implements PipeTransform {
  transform(allCategories: Category[], filter: string) {
    return allCategories.filter(category => category.topCategory == filter);
  }
}