import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GenderComponent } from '../list/gender.component';
import { GenderDetailComponent } from '../detail/gender-detail.component';
import { GenderUpdateComponent } from '../update/gender-update.component';
import { GenderRoutingResolveService } from './gender-routing-resolve.service';

const genderRoute: Routes = [
  {
    path: '',
    component: GenderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GenderDetailComponent,
    resolve: {
      gender: GenderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GenderUpdateComponent,
    resolve: {
      gender: GenderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GenderUpdateComponent,
    resolve: {
      gender: GenderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(genderRoute)],
  exports: [RouterModule],
})
export class GenderRoutingModule {}
