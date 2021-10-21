import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DietNatureComponent } from '../list/diet-nature.component';
import { DietNatureDetailComponent } from '../detail/diet-nature-detail.component';
import { DietNatureUpdateComponent } from '../update/diet-nature-update.component';
import { DietNatureRoutingResolveService } from './diet-nature-routing-resolve.service';

const dietNatureRoute: Routes = [
  {
    path: '',
    component: DietNatureComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DietNatureDetailComponent,
    resolve: {
      dietNature: DietNatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DietNatureUpdateComponent,
    resolve: {
      dietNature: DietNatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DietNatureUpdateComponent,
    resolve: {
      dietNature: DietNatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dietNatureRoute)],
  exports: [RouterModule],
})
export class DietNatureRoutingModule {}
