import { Component, OnInit, ElementRef } from '@angular/core';
// import * as annotatorjs from 'annotatorjs';

declare var $:any;

@Component({
    moduleId: module.id,
    selector: 'main-app',
    templateUrl: 'main.component.html'
})
export class MainComponent implements OnInit{
  constructor (private elementRef: ElementRef) {}

  ngOnInit () {
    $(this.elementRef.nativeElement).annotator();
  }
}
