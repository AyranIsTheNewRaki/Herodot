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

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private alertService: AlertService) { }

  ngOnInit() {
    this.userService.logout();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'home';
  }

  login(): void {
    this.loading = true;
    this.userService.tryLogin(this.model.username, this.model.password).subscribe(data => {
      if (data != null) {
        this.router.navigate([this.returnUrl]);
      } else {
        this.alertService.error("Invalid username or password!");
        this.loading = false;
      }
    }, error => {
      this.alertService.error("Invalid username or password! => " + error);
      this.loading = false;
    });
  }
}
