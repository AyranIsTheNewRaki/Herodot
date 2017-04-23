import { Injectable } from '@angular/core'
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

// Statics
import 'rxjs/add/observable/throw';
// Operators
import 'rxjs/add/operator/catch';

import { Annotation } from '../objects/annotation';
import {Observer} from "rxjs";

@Injectable()
export class AnnotationService {

  //backendUrl = 'localhost:3000';

  constructor(private http: Http) {
  }

  public getAnnotations(url: string): Annotation
  {
    return new Annotation('data value', 'target value');

    // return this.http.post().map(this.onAnnotationReceived)
    //   .catch(this.handleAnnotationError);
  }


  private onAnnotationReceived(res: Response) {
    let body = res.json();
    return new Annotation(body['data'],
      body["target"]);
  }

  public postAnnotation() {
    // return this.http.post()
    //   .map(this.onAnnotationReceived)
    //   .catch(this.handleAnnotationError);
  }

  private handleAnnotationError(error: Response) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = JSON.stringify(error);
    }
    return Observable.throw(errMsg);
  }
}
