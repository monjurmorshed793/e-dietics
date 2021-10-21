import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INutritionState, getNutritionStateIdentifier } from '../nutrition-state.model';

export type EntityResponseType = HttpResponse<INutritionState>;
export type EntityArrayResponseType = HttpResponse<INutritionState[]>;

@Injectable({ providedIn: 'root' })
export class NutritionStateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nutrition-states');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nutritionState: INutritionState): Observable<EntityResponseType> {
    return this.http.post<INutritionState>(this.resourceUrl, nutritionState, { observe: 'response' });
  }

  update(nutritionState: INutritionState): Observable<EntityResponseType> {
    return this.http.put<INutritionState>(`${this.resourceUrl}/${getNutritionStateIdentifier(nutritionState) as string}`, nutritionState, {
      observe: 'response',
    });
  }

  partialUpdate(nutritionState: INutritionState): Observable<EntityResponseType> {
    return this.http.patch<INutritionState>(
      `${this.resourceUrl}/${getNutritionStateIdentifier(nutritionState) as string}`,
      nutritionState,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<INutritionState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INutritionState[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNutritionStateToCollectionIfMissing(
    nutritionStateCollection: INutritionState[],
    ...nutritionStatesToCheck: (INutritionState | null | undefined)[]
  ): INutritionState[] {
    const nutritionStates: INutritionState[] = nutritionStatesToCheck.filter(isPresent);
    if (nutritionStates.length > 0) {
      const nutritionStateCollectionIdentifiers = nutritionStateCollection.map(
        nutritionStateItem => getNutritionStateIdentifier(nutritionStateItem)!
      );
      const nutritionStatesToAdd = nutritionStates.filter(nutritionStateItem => {
        const nutritionStateIdentifier = getNutritionStateIdentifier(nutritionStateItem);
        if (nutritionStateIdentifier == null || nutritionStateCollectionIdentifiers.includes(nutritionStateIdentifier)) {
          return false;
        }
        nutritionStateCollectionIdentifiers.push(nutritionStateIdentifier);
        return true;
      });
      return [...nutritionStatesToAdd, ...nutritionStateCollection];
    }
    return nutritionStateCollection;
  }
}
