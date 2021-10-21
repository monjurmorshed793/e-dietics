import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDietNature, DietNature } from '../diet-nature.model';
import { DietNatureService } from '../service/diet-nature.service';

@Injectable({ providedIn: 'root' })
export class DietNatureRoutingResolveService implements Resolve<IDietNature> {
  constructor(protected service: DietNatureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDietNature> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dietNature: HttpResponse<DietNature>) => {
          if (dietNature.body) {
            return of(dietNature.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DietNature());
  }
}
