import { Component } from '@angular/core';

import { CATEGORIES } from '../../data/categories';

@Component({
    moduleId: module.id,
    selector: 'category-list',
    templateUrl: 'category-list.component.html'
})
export class CategoryListComponent {
    categories = CATEGORIES;
}