import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComponentNavigation, getComponentNavigationIdentifier } from '../component-navigation.model';

export type EntityResponseType = HttpResponse<IComponentNavigation>;
export type EntityArrayResponseType = HttpResponse<IComponentNavigation[]>;

@Injectable({ providedIn: 'root' })
export class ComponentNavigationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/component-navigations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(componentNavigation: IComponentNavigation): Observable<EntityResponseType> {
    return this.http.post<IComponentNavigation>(this.resourceUrl, componentNavigation, { observe: 'response' });
  }

  update(componentNavigation: IComponentNavigation): Observable<EntityResponseType> {
    return this.http.put<IComponentNavigation>(
      `${this.resourceUrl}/${getComponentNavigationIdentifier(componentNavigation) as string}`,
      componentNavigation,
      { observe: 'response' }
    );
  }

  partialUpdate(componentNavigation: IComponentNavigation): Observable<EntityResponseType> {
    return this.http.patch<IComponentNavigation>(
      `${this.resourceUrl}/${getComponentNavigationIdentifier(componentNavigation) as string}`,
      componentNavigation,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IComponentNavigation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComponentNavigation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComponentNavigationToCollectionIfMissing(
    componentNavigationCollection: IComponentNavigation[],
    ...componentNavigationsToCheck: (IComponentNavigation | null | undefined)[]
  ): IComponentNavigation[] {
    const componentNavigations: IComponentNavigation[] = componentNavigationsToCheck.filter(isPresent);
    if (componentNavigations.length > 0) {
      const componentNavigationCollectionIdentifiers = componentNavigationCollection.map(
        componentNavigationItem => getComponentNavigationIdentifier(componentNavigationItem)!
      );
      const componentNavigationsToAdd = componentNavigations.filter(componentNavigationItem => {
        const componentNavigationIdentifier = getComponentNavigationIdentifier(componentNavigationItem);
        if (componentNavigationIdentifier == null || componentNavigationCollectionIdentifiers.includes(componentNavigationIdentifier)) {
          return false;
        }
        componentNavigationCollectionIdentifiers.push(componentNavigationIdentifier);
        return true;
      });
      return [...componentNavigationsToAdd, ...componentNavigationCollection];
    }
    return componentNavigationCollection;
  }
}
