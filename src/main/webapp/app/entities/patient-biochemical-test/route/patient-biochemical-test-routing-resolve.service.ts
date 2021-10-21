import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatientBiochemicalTest, PatientBiochemicalTest } from '../patient-biochemical-test.model';
import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';

@Injectable({ providedIn: 'root' })
export class PatientBiochemicalTestRoutingResolveService implements Resolve<IPatientBiochemicalTest> {
  constructor(protected service: PatientBiochemicalTestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatientBiochemicalTest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patientBiochemicalTest: HttpResponse<PatientBiochemicalTest>) => {
          if (patientBiochemicalTest.body) {
            return of(patientBiochemicalTest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PatientBiochemicalTest());
  }
}
