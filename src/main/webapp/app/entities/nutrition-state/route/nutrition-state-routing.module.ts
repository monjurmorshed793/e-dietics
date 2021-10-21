import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NutritionStateComponent } from '../list/nutrition-state.component';
import { NutritionStateDetailComponent } from '../detail/nutrition-state-detail.component';
import { NutritionStateUpdateComponent } from '../update/nutrition-state-update.component';
import { NutritionStateRoutingResolveService } from './nutrition-state-routing-resolve.service';

const nutritionStateRoute: Routes = [
  {
    path: '',
    component: NutritionStateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NutritionStateDetailComponent,
    resolve: {
      nutritionState: NutritionStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NutritionStateUpdateComponent,
    resolve: {
      nutritionState: NutritionStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NutritionStateUpdateComponent,
    resolve: {
      nutritionState: NutritionStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nutritionStateRoute)],
  exports: [RouterModule],
})
export class NutritionStateRoutingModule {}
