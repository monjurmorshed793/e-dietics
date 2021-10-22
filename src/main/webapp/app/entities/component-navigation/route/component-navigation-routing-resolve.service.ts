import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComponentNavigation, ComponentNavigation } from '../component-navigation.model';
import { ComponentNavigationService } from '../service/component-navigation.service';

@Injectable({ providedIn: 'root' })
export class ComponentNavigationRoutingResolveService implements Resolve<IComponentNavigation> {
  constructor(protected service: ComponentNavigationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComponentNavigation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((componentNavigation: HttpResponse<ComponentNavigation>) => {
          if (componentNavigation.body) {
            return of(componentNavigation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ComponentNavigation());
  }
}
