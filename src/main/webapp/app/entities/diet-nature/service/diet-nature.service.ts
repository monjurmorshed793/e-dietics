import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDietNature, getDietNatureIdentifier } from '../diet-nature.model';

export type EntityResponseType = HttpResponse<IDietNature>;
export type EntityArrayResponseType = HttpResponse<IDietNature[]>;

@Injectable({ providedIn: 'root' })
export class DietNatureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/diet-natures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dietNature: IDietNature): Observable<EntityResponseType> {
    return this.http.post<IDietNature>(this.resourceUrl, dietNature, { observe: 'response' });
  }

  update(dietNature: IDietNature): Observable<EntityResponseType> {
    return this.http.put<IDietNature>(`${this.resourceUrl}/${getDietNatureIdentifier(dietNature) as string}`, dietNature, {
      observe: 'response',
    });
  }

  partialUpdate(dietNature: IDietNature): Observable<EntityResponseType> {
    return this.http.patch<IDietNature>(`${this.resourceUrl}/${getDietNatureIdentifier(dietNature) as string}`, dietNature, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDietNature>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDietNature[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDietNatureToCollectionIfMissing(
    dietNatureCollection: IDietNature[],
    ...dietNaturesToCheck: (IDietNature | null | undefined)[]
  ): IDietNature[] {
    const dietNatures: IDietNature[] = dietNaturesToCheck.filter(isPresent);
    if (dietNatures.length > 0) {
      const dietNatureCollectionIdentifiers = dietNatureCollection.map(dietNatureItem => getDietNatureIdentifier(dietNatureItem)!);
      const dietNaturesToAdd = dietNatures.filter(dietNatureItem => {
        const dietNatureIdentifier = getDietNatureIdentifier(dietNatureItem);
        if (dietNatureIdentifier == null || dietNatureCollectionIdentifiers.includes(dietNatureIdentifier)) {
          return false;
        }
        dietNatureCollectionIdentifiers.push(dietNatureIdentifier);
        return true;
      });
      return [...dietNaturesToAdd, ...dietNatureCollection];
    }
    return dietNatureCollection;
  }
}
