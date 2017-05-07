import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map'

import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Rx';

import { UserInfo } from '../objects/userinfo';
import { UserRegistration } from '../objects/userRegistration';
import { OperationResponse } from '../objects/operationResponse';

@Injectable()
export class UserService implements CanActivate {

  private isLoggedInSource = new Subject<boolean>();

  constructor(private http: Http) {

  }

  getUser(): UserInfo {
    return <UserInfo>JSON.parse(localStorage.getItem("currentUser"));
  }

  logout(): void {
    localStorage.removeItem("currentUser");
    this.isLoggedInSource.next(false);
  }

  tryLogin(username: string, password: string): Observable<UserInfo> {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post('http://api.herodot.world/auth', JSON.stringify({ username: username, password: password }), options)
      .map((response: Response) => {
        console.log("Response => " + response.json());
        let res = response.json();
        if (res && res.token) {
          var userInfo = new UserInfo();
          userInfo.userName = username;
          userInfo.token = res.token;
          localStorage.setItem('currentUser', JSON.stringify(userInfo));
          this.isLoggedInSource.next(true);
          return userInfo;
        } else {
          return null;
        }
      });
  }

  isLoggedIn(): boolean {
    if (localStorage.getItem("currentUser"))
      return true;
    return false;
  }

  loginStatusChanged(): Observable<boolean> {
    return this.isLoggedInSource.asObservable();
  }

  register(model: UserRegistration) {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post('http://api.herodot.world/register', JSON.stringify({ username: model.username, password: model.password, email: model.email }), options);
  }

  canActivate() {
    return this.isLoggedIn();
  }
}
