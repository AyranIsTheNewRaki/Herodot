import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Cho } from '../../objects/cho';
import { ChoService } from '../../services/cho.service';
import { MapsService } from '../../services/maps.service';
import { AlertService } from '../../services/alert.service';

@Component({
    moduleId: module.id,
    selector: 'detail',
    templateUrl: 'detail.component.html'
})
export class DetailComponent {
    item: Cho;
    requestedId : number;    

    constructor(private route: Router, private choService: ChoService, private alertService: AlertService, private mapsService: MapsService) {
        this.requestedId = Number(route.parseUrl(route.url).queryParams["id"]);
    }

    ngOnInit() {
        this.choService.getCho(this.requestedId).subscribe(data => {
            this.item = data;
            console.log(this.item);
            if(this.item.timeLocations.length > 0)
            {
                this.mapsService.initMap(document.getElementById('map'), true, 51.508742, -0.120850);
                for(let i = 0; i < this.item.timeLocations.length; i++)
                {
                    this.mapsService.addShape(this.item.timeLocations[i].shape, i===0);
                }
            }
            else
            {
                document.getElementById('map').innerHTML = "No time location defined for this CHO.";
            }
        }, error => {
            this.alertService.error("Error while getting Cho object! Please try again later");
            console.log(error);
        });
    }
}