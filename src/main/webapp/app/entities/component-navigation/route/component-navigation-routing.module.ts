import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComponentNavigationComponent } from '../list/component-navigation.component';
import { ComponentNavigationDetailComponent } from '../detail/component-navigation-detail.component';
import { ComponentNavigationUpdateComponent } from '../update/component-navigation-update.component';
import { ComponentNavigationRoutingResolveService } from './component-navigation-routing-resolve.service';

const componentNavigationRoute: Routes = [
  {
    path: '',
    component: ComponentNavigationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComponentNavigationDetailComponent,
    resolve: {
      componentNavigation: ComponentNavigationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComponentNavigationUpdateComponent,
    resolve: {
      componentNavigation: ComponentNavigationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComponentNavigationUpdateComponent,
    resolve: {
      componentNavigation: ComponentNavigationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(componentNavigationRoute)],
  exports: [RouterModule],
})
export class ComponentNavigationRoutingModule {}
