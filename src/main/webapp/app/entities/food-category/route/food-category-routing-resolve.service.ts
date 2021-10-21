import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFoodCategory, FoodCategory } from '../food-category.model';
import { FoodCategoryService } from '../service/food-category.service';

@Injectable({ providedIn: 'root' })
export class FoodCategoryRoutingResolveService implements Resolve<IFoodCategory> {
  constructor(protected service: FoodCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFoodCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((foodCategory: HttpResponse<FoodCategory>) => {
          if (foodCategory.body) {
            return of(foodCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FoodCategory());
  }
}
