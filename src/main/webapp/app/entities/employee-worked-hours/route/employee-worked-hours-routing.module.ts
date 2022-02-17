import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeWorkedHoursComponent } from '../list/employee-worked-hours.component';
import { EmployeeWorkedHoursDetailComponent } from '../detail/employee-worked-hours-detail.component';
import { EmployeeWorkedHoursUpdateComponent } from '../update/employee-worked-hours-update.component';
import { EmployeeWorkedHoursRoutingResolveService } from './employee-worked-hours-routing-resolve.service';

const employeeWorkedHoursRoute: Routes = [
  {
    path: '',
    component: EmployeeWorkedHoursComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeWorkedHoursDetailComponent,
    resolve: {
      employeeWorkedHours: EmployeeWorkedHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeWorkedHoursUpdateComponent,
    resolve: {
      employeeWorkedHours: EmployeeWorkedHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeWorkedHoursUpdateComponent,
    resolve: {
      employeeWorkedHours: EmployeeWorkedHoursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeWorkedHoursRoute)],
  exports: [RouterModule],
})
export class EmployeeWorkedHoursRoutingModule {}
