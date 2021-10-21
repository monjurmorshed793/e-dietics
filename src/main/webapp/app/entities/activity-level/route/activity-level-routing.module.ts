import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivityLevelComponent } from '../list/activity-level.component';
import { ActivityLevelDetailComponent } from '../detail/activity-level-detail.component';
import { ActivityLevelUpdateComponent } from '../update/activity-level-update.component';
import { ActivityLevelRoutingResolveService } from './activity-level-routing-resolve.service';

const activityLevelRoute: Routes = [
  {
    path: '',
    component: ActivityLevelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityLevelDetailComponent,
    resolve: {
      activityLevel: ActivityLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivityLevelUpdateComponent,
    resolve: {
      activityLevel: ActivityLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivityLevelUpdateComponent,
    resolve: {
      activityLevel: ActivityLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activityLevelRoute)],
  exports: [RouterModule],
})
export class ActivityLevelRoutingModule {}
