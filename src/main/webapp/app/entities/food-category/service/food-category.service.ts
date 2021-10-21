import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFoodCategory, getFoodCategoryIdentifier } from '../food-category.model';

export type EntityResponseType = HttpResponse<IFoodCategory>;
export type EntityArrayResponseType = HttpResponse<IFoodCategory[]>;

@Injectable({ providedIn: 'root' })
export class FoodCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/food-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(foodCategory: IFoodCategory): Observable<EntityResponseType> {
    return this.http.post<IFoodCategory>(this.resourceUrl, foodCategory, { observe: 'response' });
  }

  update(foodCategory: IFoodCategory): Observable<EntityResponseType> {
    return this.http.put<IFoodCategory>(`${this.resourceUrl}/${getFoodCategoryIdentifier(foodCategory) as string}`, foodCategory, {
      observe: 'response',
    });
  }

  partialUpdate(foodCategory: IFoodCategory): Observable<EntityResponseType> {
    return this.http.patch<IFoodCategory>(`${this.resourceUrl}/${getFoodCategoryIdentifier(foodCategory) as string}`, foodCategory, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFoodCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFoodCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFoodCategoryToCollectionIfMissing(
    foodCategoryCollection: IFoodCategory[],
    ...foodCategoriesToCheck: (IFoodCategory | null | undefined)[]
  ): IFoodCategory[] {
    const foodCategories: IFoodCategory[] = foodCategoriesToCheck.filter(isPresent);
    if (foodCategories.length > 0) {
      const foodCategoryCollectionIdentifiers = foodCategoryCollection.map(
        foodCategoryItem => getFoodCategoryIdentifier(foodCategoryItem)!
      );
      const foodCategoriesToAdd = foodCategories.filter(foodCategoryItem => {
        const foodCategoryIdentifier = getFoodCategoryIdentifier(foodCategoryItem);
        if (foodCategoryIdentifier == null || foodCategoryCollectionIdentifiers.includes(foodCategoryIdentifier)) {
          return false;
        }
        foodCategoryCollectionIdentifiers.push(foodCategoryIdentifier);
        return true;
      });
      return [...foodCategoriesToAdd, ...foodCategoryCollection];
    }
    return foodCategoryCollection;
  }
}
