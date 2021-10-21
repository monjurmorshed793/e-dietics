import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISupplements, Supplements } from '../supplements.model';
import { SupplementsService } from '../service/supplements.service';

@Injectable({ providedIn: 'root' })
export class SupplementsRoutingResolveService implements Resolve<ISupplements> {
  constructor(protected service: SupplementsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISupplements> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((supplements: HttpResponse<Supplements>) => {
          if (supplements.body) {
            return of(supplements.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Supplements());
  }
}
