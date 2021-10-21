jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBiochemicalTest, BiochemicalTest } from '../biochemical-test.model';
import { BiochemicalTestService } from '../service/biochemical-test.service';

import { BiochemicalTestRoutingResolveService } from './biochemical-test-routing-resolve.service';

describe('BiochemicalTest routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BiochemicalTestRoutingResolveService;
  let service: BiochemicalTestService;
  let resultBiochemicalTest: IBiochemicalTest | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BiochemicalTestRoutingResolveService);
    service = TestBed.inject(BiochemicalTestService);
    resultBiochemicalTest = undefined;
  });

  describe('resolve', () => {
    it('should return IBiochemicalTest returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBiochemicalTest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultBiochemicalTest).toEqual({ id: 'ABC' });
    });

    it('should return new IBiochemicalTest if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBiochemicalTest = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBiochemicalTest).toEqual(new BiochemicalTest());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BiochemicalTest })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBiochemicalTest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultBiochemicalTest).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
