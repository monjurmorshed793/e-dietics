jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFoodCategory, FoodCategory } from '../food-category.model';
import { FoodCategoryService } from '../service/food-category.service';

import { FoodCategoryRoutingResolveService } from './food-category-routing-resolve.service';

describe('FoodCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FoodCategoryRoutingResolveService;
  let service: FoodCategoryService;
  let resultFoodCategory: IFoodCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FoodCategoryRoutingResolveService);
    service = TestBed.inject(FoodCategoryService);
    resultFoodCategory = undefined;
  });

  describe('resolve', () => {
    it('should return IFoodCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultFoodCategory).toEqual({ id: 'ABC' });
    });

    it('should return new IFoodCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFoodCategory).toEqual(new FoodCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FoodCategory })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultFoodCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
