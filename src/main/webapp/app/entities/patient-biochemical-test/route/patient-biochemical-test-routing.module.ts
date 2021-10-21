import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatientBiochemicalTestComponent } from '../list/patient-biochemical-test.component';
import { PatientBiochemicalTestDetailComponent } from '../detail/patient-biochemical-test-detail.component';
import { PatientBiochemicalTestUpdateComponent } from '../update/patient-biochemical-test-update.component';
import { PatientBiochemicalTestRoutingResolveService } from './patient-biochemical-test-routing-resolve.service';

const patientBiochemicalTestRoute: Routes = [
  {
    path: '',
    component: PatientBiochemicalTestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientBiochemicalTestDetailComponent,
    resolve: {
      patientBiochemicalTest: PatientBiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatientBiochemicalTestUpdateComponent,
    resolve: {
      patientBiochemicalTest: PatientBiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientBiochemicalTestUpdateComponent,
    resolve: {
      patientBiochemicalTest: PatientBiochemicalTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patientBiochemicalTestRoute)],
  exports: [RouterModule],
})
export class PatientBiochemicalTestRoutingModule {}
