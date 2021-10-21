import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatientBiochemicalTest, getPatientBiochemicalTestIdentifier } from '../patient-biochemical-test.model';

export type EntityResponseType = HttpResponse<IPatientBiochemicalTest>;
export type EntityArrayResponseType = HttpResponse<IPatientBiochemicalTest[]>;

@Injectable({ providedIn: 'root' })
export class PatientBiochemicalTestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patient-biochemical-tests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(patientBiochemicalTest: IPatientBiochemicalTest): Observable<EntityResponseType> {
    return this.http.post<IPatientBiochemicalTest>(this.resourceUrl, patientBiochemicalTest, { observe: 'response' });
  }

  update(patientBiochemicalTest: IPatientBiochemicalTest): Observable<EntityResponseType> {
    return this.http.put<IPatientBiochemicalTest>(
      `${this.resourceUrl}/${getPatientBiochemicalTestIdentifier(patientBiochemicalTest) as string}`,
      patientBiochemicalTest,
      { observe: 'response' }
    );
  }

  partialUpdate(patientBiochemicalTest: IPatientBiochemicalTest): Observable<EntityResponseType> {
    return this.http.patch<IPatientBiochemicalTest>(
      `${this.resourceUrl}/${getPatientBiochemicalTestIdentifier(patientBiochemicalTest) as string}`,
      patientBiochemicalTest,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPatientBiochemicalTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPatientBiochemicalTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatientBiochemicalTestToCollectionIfMissing(
    patientBiochemicalTestCollection: IPatientBiochemicalTest[],
    ...patientBiochemicalTestsToCheck: (IPatientBiochemicalTest | null | undefined)[]
  ): IPatientBiochemicalTest[] {
    const patientBiochemicalTests: IPatientBiochemicalTest[] = patientBiochemicalTestsToCheck.filter(isPresent);
    if (patientBiochemicalTests.length > 0) {
      const patientBiochemicalTestCollectionIdentifiers = patientBiochemicalTestCollection.map(
        patientBiochemicalTestItem => getPatientBiochemicalTestIdentifier(patientBiochemicalTestItem)!
      );
      const patientBiochemicalTestsToAdd = patientBiochemicalTests.filter(patientBiochemicalTestItem => {
        const patientBiochemicalTestIdentifier = getPatientBiochemicalTestIdentifier(patientBiochemicalTestItem);
        if (
          patientBiochemicalTestIdentifier == null ||
          patientBiochemicalTestCollectionIdentifiers.includes(patientBiochemicalTestIdentifier)
        ) {
          return false;
        }
        patientBiochemicalTestCollectionIdentifiers.push(patientBiochemicalTestIdentifier);
        return true;
      });
      return [...patientBiochemicalTestsToAdd, ...patientBiochemicalTestCollection];
    }
    return patientBiochemicalTestCollection;
  }
}
