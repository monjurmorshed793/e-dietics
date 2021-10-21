jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INutritionState, NutritionState } from '../nutrition-state.model';
import { NutritionStateService } from '../service/nutrition-state.service';

import { NutritionStateRoutingResolveService } from './nutrition-state-routing-resolve.service';

describe('NutritionState routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NutritionStateRoutingResolveService;
  let service: NutritionStateService;
  let resultNutritionState: INutritionState | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NutritionStateRoutingResolveService);
    service = TestBed.inject(NutritionStateService);
    resultNutritionState = undefined;
  });

  describe('resolve', () => {
    it('should return INutritionState returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNutritionState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultNutritionState).toEqual({ id: 'ABC' });
    });

    it('should return new INutritionState if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNutritionState = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNutritionState).toEqual(new NutritionState());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NutritionState })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNutritionState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultNutritionState).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
