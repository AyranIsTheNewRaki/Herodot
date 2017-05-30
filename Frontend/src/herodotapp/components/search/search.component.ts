import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Cho } from '../../objects/cho';
import { ChoService } from '../../services/cho.service';
import { AlertService } from '../../services/alert.service';

@Component({
    moduleId: module.id,
    selector: 'search',
    templateUrl: 'search.component.html'
})
export class SearchComponent {
    query: string;
    items: Cho[];

    constructor(private route: Router, private choService: ChoService, private alertService: AlertService) {
        this.query = route.parseUrl(route.url).queryParams["query"];
        console.log(this.query);
    }

    ngOnInit() {
        this.choService.getChos().subscribe(data => {
            this.items = [];
            let search = new RegExp(this.query, "i");
            this.items = data.filter(cho => cho.title.search(search) !== -1);
        }, error => {
            this.alertService.error("Error while getting Cho objects! Please try again later");
            console.log(error);
        })
    }
}