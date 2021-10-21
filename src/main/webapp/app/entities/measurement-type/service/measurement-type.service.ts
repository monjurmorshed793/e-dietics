import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeasurementType, getMeasurementTypeIdentifier } from '../measurement-type.model';

export type EntityResponseType = HttpResponse<IMeasurementType>;
export type EntityArrayResponseType = HttpResponse<IMeasurementType[]>;

@Injectable({ providedIn: 'root' })
export class MeasurementTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/measurement-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(measurementType: IMeasurementType): Observable<EntityResponseType> {
    return this.http.post<IMeasurementType>(this.resourceUrl, measurementType, { observe: 'response' });
  }

  update(measurementType: IMeasurementType): Observable<EntityResponseType> {
    return this.http.put<IMeasurementType>(
      `${this.resourceUrl}/${getMeasurementTypeIdentifier(measurementType) as string}`,
      measurementType,
      { observe: 'response' }
    );
  }

  partialUpdate(measurementType: IMeasurementType): Observable<EntityResponseType> {
    return this.http.patch<IMeasurementType>(
      `${this.resourceUrl}/${getMeasurementTypeIdentifier(measurementType) as string}`,
      measurementType,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IMeasurementType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeasurementType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeasurementTypeToCollectionIfMissing(
    measurementTypeCollection: IMeasurementType[],
    ...measurementTypesToCheck: (IMeasurementType | null | undefined)[]
  ): IMeasurementType[] {
    const measurementTypes: IMeasurementType[] = measurementTypesToCheck.filter(isPresent);
    if (measurementTypes.length > 0) {
      const measurementTypeCollectionIdentifiers = measurementTypeCollection.map(
        measurementTypeItem => getMeasurementTypeIdentifier(measurementTypeItem)!
      );
      const measurementTypesToAdd = measurementTypes.filter(measurementTypeItem => {
        const measurementTypeIdentifier = getMeasurementTypeIdentifier(measurementTypeItem);
        if (measurementTypeIdentifier == null || measurementTypeCollectionIdentifiers.includes(measurementTypeIdentifier)) {
          return false;
        }
        measurementTypeCollectionIdentifiers.push(measurementTypeIdentifier);
        return true;
      });
      return [...measurementTypesToAdd, ...measurementTypeCollection];
    }
    return measurementTypeCollection;
  }
}
