import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeasurementTypeComponent } from '../list/measurement-type.component';
import { MeasurementTypeDetailComponent } from '../detail/measurement-type-detail.component';
import { MeasurementTypeUpdateComponent } from '../update/measurement-type-update.component';
import { MeasurementTypeRoutingResolveService } from './measurement-type-routing-resolve.service';

const measurementTypeRoute: Routes = [
  {
    path: '',
    component: MeasurementTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeasurementTypeDetailComponent,
    resolve: {
      measurementType: MeasurementTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeasurementTypeUpdateComponent,
    resolve: {
      measurementType: MeasurementTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeasurementTypeUpdateComponent,
    resolve: {
      measurementType: MeasurementTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(measurementTypeRoute)],
  exports: [RouterModule],
})
export class MeasurementTypeRoutingModule {}
