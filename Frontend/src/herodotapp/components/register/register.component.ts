import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService } from '../../services/alert.service';
import { UserService } from '../../services/user.service';
import { UserRegistration } from '../../objects/userRegistration';

@Component({
    moduleId: module.id,
    selector: 'register',
    templateUrl: 'register.component.html'
})
export class RegisterComponent {
    model: UserRegistration = new UserRegistration();
    loading = false;

    constructor(private router: Router, private userService: UserService, private alertService: AlertService) {

    }

    register(): void {
        this.loading = true;
        if(this.model.password !== this.model.repassword){
            this.alertService.error("Passwords do not match!");
            this .loading = false;
            return;
        }
        this.userService.register(this.model).subscribe( data => {
            this.alertService.success("Registration was successfull.", true);
            this.router.navigate(['login']);
        }, error => {
            if(error.status === 500){
                console.log(error);
                this.alertService.error(JSON.parse(error._body).message);
                this.loading = false;
            }
        });
    }
}
