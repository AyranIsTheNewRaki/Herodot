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

  constructor(private route: ActivatedRoute, private router: Router,private userService: UserService, private alertService: AlertService) { }

  ngOnInit(){
    this.userService.logout();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'home';
  }

  login(): void {
    console.log('login 1');
    this.loading = true;
    if(this.userService.tryLogin(this.model.username, this.model.password)){
      console.log('login 2');
      if(this.returnUrl) {
        console.log('login 3', this.returnUrl);
        this.router.navigate([this.returnUrl]);
      }
    }
    else {
      console.log('login 4');
      this.alertService.error("Invalid username or password!");
      this.loading = false;
    }
  }
}
