import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CATEGORIES } from '../../data/categories';

@Component({
    moduleId: module.id,
    selector: 'herodot-home',
    templateUrl: 'home.component.html'
})
export class HomeComponent {
  categories = CATEGORIES;

  constructor (private route: ActivatedRoute) {
    console.log('home component const')
  }
  ngOnInit () {
    this.route.data.subscribe(x =>
      {
        console.log('route data recieved: ', x);
      }
    );
  }
}
