import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';

import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Rx';

import { UserInfo } from '../objects/userinfo';
import { UserRegistration } from '../objects/userRegistration';
import { OperationResponse } from '../objects/operationResponse';

@Injectable()
export class UserService implements CanActivate {

  private isLoggedInSource = new Subject<boolean>();

  getUser(): UserInfo {
    return <UserInfo>JSON.parse(localStorage.getItem("currentUser"));
  }

  logout(): void {
    localStorage.removeItem("currentUser");
    this.isLoggedInSource.next(false);
  }

  tryLogin(username: string, password: string): boolean {
    if (username === "TestUser" && password === "123") {
        var userInfo = new UserInfo();
        userInfo.userName = username;
        userInfo.token = "1234";
        userInfo.id = 123;
        localStorage.setItem("currentUser", JSON.stringify(userInfo));
        this.isLoggedInSource.next(true);
        return true;
    }
    return false;
  }

  isLoggedIn(): boolean {
    if (localStorage.getItem("currentUser"))
      return true;
    return false;
  }

  loginStatusChanged(): Observable<boolean> {
    return this.isLoggedInSource.asObservable();
  }

  register(model: UserRegistration): OperationResponse{
    if(model.username === "fail"){
      return new OperationResponse(false, "Username is fail!");
    }
    return new OperationResponse(true);
  }

  canActivate() {
    return this.isLoggedIn();
  }
}
