import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeasurementType, MeasurementType } from '../measurement-type.model';
import { MeasurementTypeService } from '../service/measurement-type.service';

@Injectable({ providedIn: 'root' })
export class MeasurementTypeRoutingResolveService implements Resolve<IMeasurementType> {
  constructor(protected service: MeasurementTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeasurementType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((measurementType: HttpResponse<MeasurementType>) => {
          if (measurementType.body) {
            return of(measurementType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeasurementType());
  }
}
