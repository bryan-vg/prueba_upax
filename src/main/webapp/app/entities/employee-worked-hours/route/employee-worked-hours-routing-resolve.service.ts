import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeWorkedHours, EmployeeWorkedHours } from '../employee-worked-hours.model';
import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';

@Injectable({ providedIn: 'root' })
export class EmployeeWorkedHoursRoutingResolveService implements Resolve<IEmployeeWorkedHours> {
  constructor(protected service: EmployeeWorkedHoursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeWorkedHours> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeWorkedHours: HttpResponse<EmployeeWorkedHours>) => {
          if (employeeWorkedHours.body) {
            return of(employeeWorkedHours.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeWorkedHours());
  }
}
