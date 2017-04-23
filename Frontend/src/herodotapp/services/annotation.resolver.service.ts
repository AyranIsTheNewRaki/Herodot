import { Injectable } from '@angular/core';
import { Router, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { Annotation } from '../objects/annotation';

import { AnnotationService } from './annotation.service';

@Injectable()
export class AnnotationResolverService implements Resolve<Annotation>{
  constructor (private annotationService: AnnotationService, private router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.annotationService.getAnnotations(this.router.url);
  }
}
