import { Component, OnInit, ElementRef } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'main-app',
  templateUrl: 'main.component.html'
})
export class MainComponent implements OnInit {
  ngOnInit() {

  }

  ngAfterViewInit() {
    let appLinkEl = document.querySelector('link[type="application/annotator+html"]');
    if (appLinkEl) appLinkEl.dispatchEvent(new Event('destroy'));

    let embedScript = document.createElement('script');
    embedScript.setAttribute('src', 'https://hypothes.is/app/embed.js');
    document.getElementsByTagName("main-app")[0].appendChild(embedScript);
    (<any>window).hypothesisConfig = function () {
      return {
        "openSidebar": false,
        "showHighlight": true
      };
    };
  }
}


/*
let appLinkEl = document.querySelector('link[type="application/annotator+html"]');
if (appLinkEl) appLinkEl.dispatchEvent(new Event('destroy'));

let embedScript = document.createElement('script');
embedScript.setAttribute('src', 'https://hypothes.is/app/embed.js');
document.getElementsByTagName("main-app")[0].appendChild(embedScript); */