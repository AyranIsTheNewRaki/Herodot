import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Cho } from '../../objects/cho';
import { ChoService } from '../../services/cho.service';
import { AlertService } from '../../services/alert.service';
import { UserService } from '../../services/user.service';

@Component({
    moduleId: module.id,
    selector: 'myheritages',
    templateUrl: 'myheritages.component.html'
})
export class MyHeritagesComponent {
    items: Cho[];

    constructor(private choService: ChoService, private userService: UserService, private alertService: AlertService) {
    }

    ngOnInit() {
        this.choService.getChos().subscribe(data => {
            this.items = [];
            let username = this.userService.getUser().userName;
            this.items = data.filter(cho => cho.username === username);
        }, error => {
            this.alertService.error("Error while getting Cho objects! Please try again later");
            console.log(error);
        })
    }
}