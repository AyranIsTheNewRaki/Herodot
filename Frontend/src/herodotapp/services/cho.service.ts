import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Rx';

import { OperationResponse } from '../objects/operationResponse';
import { Cho } from '../objects/cho';
import { UserService } from '../services/user.service';

@Injectable()
export class ChoService {

    constructor(private http: Http, private userService: UserService) {

    }

    addCho(cho: Cho) {
        let headers = new Headers({ 'Content-Type': 'application/json', 'Authorization': this.userService.getUser().token });
        let options = new RequestOptions({ headers: headers });
        let requestJson = cho.requestJson();
        console.log(requestJson);
        console.log(JSON.stringify(headers));
        return this.http.post('http://api.herodot.world/heritage', requestJson, options);
    }

    getChos() : Observable<Cho[]>{
        let headers = new Headers({ 'Content-Type': 'application/json', 'Authorization': this.userService.getUser().token });
        let options = new RequestOptions({ headers: headers });
        return this.http.get('http://api.herodot.world/heritage', options).map((response: Response) => {
            return <Cho[]>response.json()
        });
    }
}