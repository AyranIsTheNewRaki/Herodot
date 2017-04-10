import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { UserService } from '../../services/user.service'
import { AlertService } from '../../services/alert.service'

@Component({
    moduleId: module.id,
    selector: 'login',
    templateUrl: 'login.component.html'
})
export class LoginComponent {
    model: any = {};
    returnUrl: string;
    loading = false;

    constructor(private route: ActivatedRoute, private router: Router,private userService: UserService, private alertService: AlertService) {

    }

    ngOnInit(){
        this.userService.logout();
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    login(): void {
        this.loading = true;
        if(this.userService.tryLogin(this.model.username, this.model.password)){
             this.router.navigate([this.returnUrl]);
        }else{
            this.alertService.error("Invalid username or password!");
            this.loading = false;
        }
    }
}