import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SupplementsComponent } from '../list/supplements.component';
import { SupplementsDetailComponent } from '../detail/supplements-detail.component';
import { SupplementsUpdateComponent } from '../update/supplements-update.component';
import { SupplementsRoutingResolveService } from './supplements-routing-resolve.service';

const supplementsRoute: Routes = [
  {
    path: '',
    component: SupplementsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SupplementsDetailComponent,
    resolve: {
      supplements: SupplementsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SupplementsUpdateComponent,
    resolve: {
      supplements: SupplementsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SupplementsUpdateComponent,
    resolve: {
      supplements: SupplementsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(supplementsRoute)],
  exports: [RouterModule],
})
export class SupplementsRoutingModule {}
