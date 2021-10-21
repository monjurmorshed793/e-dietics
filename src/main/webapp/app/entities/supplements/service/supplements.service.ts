import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISupplements, getSupplementsIdentifier } from '../supplements.model';

export type EntityResponseType = HttpResponse<ISupplements>;
export type EntityArrayResponseType = HttpResponse<ISupplements[]>;

@Injectable({ providedIn: 'root' })
export class SupplementsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/supplements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(supplements: ISupplements): Observable<EntityResponseType> {
    return this.http.post<ISupplements>(this.resourceUrl, supplements, { observe: 'response' });
  }

  update(supplements: ISupplements): Observable<EntityResponseType> {
    return this.http.put<ISupplements>(`${this.resourceUrl}/${getSupplementsIdentifier(supplements) as string}`, supplements, {
      observe: 'response',
    });
  }

  partialUpdate(supplements: ISupplements): Observable<EntityResponseType> {
    return this.http.patch<ISupplements>(`${this.resourceUrl}/${getSupplementsIdentifier(supplements) as string}`, supplements, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISupplements>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISupplements[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSupplementsToCollectionIfMissing(
    supplementsCollection: ISupplements[],
    ...supplementsToCheck: (ISupplements | null | undefined)[]
  ): ISupplements[] {
    const supplements: ISupplements[] = supplementsToCheck.filter(isPresent);
    if (supplements.length > 0) {
      const supplementsCollectionIdentifiers = supplementsCollection.map(supplementsItem => getSupplementsIdentifier(supplementsItem)!);
      const supplementsToAdd = supplements.filter(supplementsItem => {
        const supplementsIdentifier = getSupplementsIdentifier(supplementsItem);
        if (supplementsIdentifier == null || supplementsCollectionIdentifiers.includes(supplementsIdentifier)) {
          return false;
        }
        supplementsCollectionIdentifiers.push(supplementsIdentifier);
        return true;
      });
      return [...supplementsToAdd, ...supplementsCollection];
    }
    return supplementsCollection;
  }
}
