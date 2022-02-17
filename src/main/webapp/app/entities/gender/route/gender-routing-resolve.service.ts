import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGender, Gender } from '../gender.model';
import { GenderService } from '../service/gender.service';

@Injectable({ providedIn: 'root' })
export class GenderRoutingResolveService implements Resolve<IGender> {
  constructor(protected service: GenderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGender> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gender: HttpResponse<Gender>) => {
          if (gender.body) {
            return of(gender.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gender());
  }
}
