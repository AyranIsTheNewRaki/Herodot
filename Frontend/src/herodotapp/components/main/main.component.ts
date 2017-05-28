import { Component, OnInit, ElementRef } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'main-app',
    templateUrl: 'main.component.html'
})
export class MainComponent implements OnInit{
  ngOnInit () {
    (<any>window).hypothesisConfig = function () {
      return {
        "openSidebar": false,
        "showHighlight": true
      };
    };
  }
}
