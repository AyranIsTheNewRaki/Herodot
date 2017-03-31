import { Component, OnInit } from '@angular/core';

import { UserService } from '../../services/user.service'

@Component({
    moduleId: module.id,
    selector: 'herodot-header',
    templateUrl: 'header.component.html'
})
export class HeaderComponent implements OnInit {
    isLoggedIn: boolean;
    username: string;

    constructor(private userService: UserService) {
        userService.loginStatusChanged().subscribe(loginStatus => this.loadUser());
    }

    ngOnInit() {
        this.loadUser();
    }

    private loadUser() : void{
        this.isLoggedIn = this.userService.isLoggedIn();
        var user = this.userService.getUser();
        if (user === null)
            this.username = "";
        else
            this.username = user.userName;
    }
}