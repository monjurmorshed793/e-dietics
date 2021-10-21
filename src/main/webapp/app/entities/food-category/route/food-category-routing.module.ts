import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FoodCategoryComponent } from '../list/food-category.component';
import { FoodCategoryDetailComponent } from '../detail/food-category-detail.component';
import { FoodCategoryUpdateComponent } from '../update/food-category-update.component';
import { FoodCategoryRoutingResolveService } from './food-category-routing-resolve.service';

const foodCategoryRoute: Routes = [
  {
    path: '',
    component: FoodCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodCategoryDetailComponent,
    resolve: {
      foodCategory: FoodCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodCategoryUpdateComponent,
    resolve: {
      foodCategory: FoodCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodCategoryUpdateComponent,
    resolve: {
      foodCategory: FoodCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(foodCategoryRoute)],
  exports: [RouterModule],
})
export class FoodCategoryRoutingModule {}
