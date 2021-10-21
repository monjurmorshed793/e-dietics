import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBiochemicalTest, BiochemicalTest } from '../biochemical-test.model';
import { BiochemicalTestService } from '../service/biochemical-test.service';

@Injectable({ providedIn: 'root' })
export class BiochemicalTestRoutingResolveService implements Resolve<IBiochemicalTest> {
  constructor(protected service: BiochemicalTestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBiochemicalTest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((biochemicalTest: HttpResponse<BiochemicalTest>) => {
          if (biochemicalTest.body) {
            return of(biochemicalTest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BiochemicalTest());
  }
}
