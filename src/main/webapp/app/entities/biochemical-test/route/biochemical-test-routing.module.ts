import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BiochemicalTestComponent } from '../list/biochemical-test.component';
import { BiochemicalTestDetailComponent } from '../detail/biochemical-test-detail.component';
import { BiochemicalTestUpdateComponent } from '../update/biochemical-test-update.component';
import { BiochemicalTestRoutingResolveService } from './biochemical-test-routing-resolve.service';

const biochemicalTestRoute: Routes = [
  {
    path: '',
    component: BiochemicalTestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BiochemicalTestDetailComponent,
    resolve: {
      biochemicalTest: BiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BiochemicalTestUpdateComponent,
    resolve: {
      biochemicalTest: BiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BiochemicalTestUpdateComponent,
    resolve: {
      biochemicalTest: BiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(biochemicalTestRoute)],
  exports: [RouterModule],
})
export class BiochemicalTestRoutingModule {}
