import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Cho } from '../../objects/cho';
import { ChoService } from '../../services/cho.service';
import { AlertService } from '../../services/alert.service';

@Component({
    moduleId: module.id,
    selector: 'category',
    templateUrl: 'category.component.html'
})
export class CategoryComponent {
    category: string;
    items: Cho[];

    constructor(private route: Router, private choService: ChoService, private alertService: AlertService) {
        this.category = route.parseUrl(route.url).queryParams["category"];
        console.log(this.category);
    }

    ngOnInit() {
        this.choService.getChos().subscribe(data => {
            this.items = [];
            this.items = data.filter(cho => cho.category === this.category);
        }, error => {
            this.alertService.error("Error while getting Cho objects! Please try again later");
            console.log(error);
        })
    }
}