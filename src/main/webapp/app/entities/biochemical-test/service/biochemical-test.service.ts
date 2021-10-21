import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBiochemicalTest, getBiochemicalTestIdentifier } from '../biochemical-test.model';

export type EntityResponseType = HttpResponse<IBiochemicalTest>;
export type EntityArrayResponseType = HttpResponse<IBiochemicalTest[]>;

@Injectable({ providedIn: 'root' })
export class BiochemicalTestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/biochemical-tests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(biochemicalTest: IBiochemicalTest): Observable<EntityResponseType> {
    return this.http.post<IBiochemicalTest>(this.resourceUrl, biochemicalTest, { observe: 'response' });
  }

  update(biochemicalTest: IBiochemicalTest): Observable<EntityResponseType> {
    return this.http.put<IBiochemicalTest>(
      `${this.resourceUrl}/${getBiochemicalTestIdentifier(biochemicalTest) as string}`,
      biochemicalTest,
      { observe: 'response' }
    );
  }

  partialUpdate(biochemicalTest: IBiochemicalTest): Observable<EntityResponseType> {
    return this.http.patch<IBiochemicalTest>(
      `${this.resourceUrl}/${getBiochemicalTestIdentifier(biochemicalTest) as string}`,
      biochemicalTest,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBiochemicalTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBiochemicalTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBiochemicalTestToCollectionIfMissing(
    biochemicalTestCollection: IBiochemicalTest[],
    ...biochemicalTestsToCheck: (IBiochemicalTest | null | undefined)[]
  ): IBiochemicalTest[] {
    const biochemicalTests: IBiochemicalTest[] = biochemicalTestsToCheck.filter(isPresent);
    if (biochemicalTests.length > 0) {
      const biochemicalTestCollectionIdentifiers = biochemicalTestCollection.map(
        biochemicalTestItem => getBiochemicalTestIdentifier(biochemicalTestItem)!
      );
      const biochemicalTestsToAdd = biochemicalTests.filter(biochemicalTestItem => {
        const biochemicalTestIdentifier = getBiochemicalTestIdentifier(biochemicalTestItem);
        if (biochemicalTestIdentifier == null || biochemicalTestCollectionIdentifiers.includes(biochemicalTestIdentifier)) {
          return false;
        }
        biochemicalTestCollectionIdentifiers.push(biochemicalTestIdentifier);
        return true;
      });
      return [...biochemicalTestsToAdd, ...biochemicalTestCollection];
    }
    return biochemicalTestCollection;
  }
}
