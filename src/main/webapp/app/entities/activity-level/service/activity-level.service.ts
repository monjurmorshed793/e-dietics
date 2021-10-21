import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivityLevel, getActivityLevelIdentifier } from '../activity-level.model';

export type EntityResponseType = HttpResponse<IActivityLevel>;
export type EntityArrayResponseType = HttpResponse<IActivityLevel[]>;

@Injectable({ providedIn: 'root' })
export class ActivityLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activity-levels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(activityLevel: IActivityLevel): Observable<EntityResponseType> {
    return this.http.post<IActivityLevel>(this.resourceUrl, activityLevel, { observe: 'response' });
  }

  update(activityLevel: IActivityLevel): Observable<EntityResponseType> {
    return this.http.put<IActivityLevel>(`${this.resourceUrl}/${getActivityLevelIdentifier(activityLevel) as string}`, activityLevel, {
      observe: 'response',
    });
  }

  partialUpdate(activityLevel: IActivityLevel): Observable<EntityResponseType> {
    return this.http.patch<IActivityLevel>(`${this.resourceUrl}/${getActivityLevelIdentifier(activityLevel) as string}`, activityLevel, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IActivityLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActivityLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActivityLevelToCollectionIfMissing(
    activityLevelCollection: IActivityLevel[],
    ...activityLevelsToCheck: (IActivityLevel | null | undefined)[]
  ): IActivityLevel[] {
    const activityLevels: IActivityLevel[] = activityLevelsToCheck.filter(isPresent);
    if (activityLevels.length > 0) {
      const activityLevelCollectionIdentifiers = activityLevelCollection.map(
        activityLevelItem => getActivityLevelIdentifier(activityLevelItem)!
      );
      const activityLevelsToAdd = activityLevels.filter(activityLevelItem => {
        const activityLevelIdentifier = getActivityLevelIdentifier(activityLevelItem);
        if (activityLevelIdentifier == null || activityLevelCollectionIdentifiers.includes(activityLevelIdentifier)) {
          return false;
        }
        activityLevelCollectionIdentifiers.push(activityLevelIdentifier);
        return true;
      });
      return [...activityLevelsToAdd, ...activityLevelCollection];
    }
    return activityLevelCollection;
  }
}
