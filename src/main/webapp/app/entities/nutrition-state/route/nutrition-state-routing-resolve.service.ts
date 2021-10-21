import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INutritionState, NutritionState } from '../nutrition-state.model';
import { NutritionStateService } from '../service/nutrition-state.service';

@Injectable({ providedIn: 'root' })
export class NutritionStateRoutingResolveService implements Resolve<INutritionState> {
  constructor(protected service: NutritionStateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INutritionState> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nutritionState: HttpResponse<NutritionState>) => {
          if (nutritionState.body) {
            return of(nutritionState.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NutritionState());
  }
}
