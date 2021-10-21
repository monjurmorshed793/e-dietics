jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMeasurementType, MeasurementType } from '../measurement-type.model';
import { MeasurementTypeService } from '../service/measurement-type.service';

import { MeasurementTypeRoutingResolveService } from './measurement-type-routing-resolve.service';

describe('MeasurementType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MeasurementTypeRoutingResolveService;
  let service: MeasurementTypeService;
  let resultMeasurementType: IMeasurementType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MeasurementTypeRoutingResolveService);
    service = TestBed.inject(MeasurementTypeService);
    resultMeasurementType = undefined;
  });

  describe('resolve', () => {
    it('should return IMeasurementType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeasurementType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultMeasurementType).toEqual({ id: 'ABC' });
    });

    it('should return new IMeasurementType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeasurementType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMeasurementType).toEqual(new MeasurementType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MeasurementType })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeasurementType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultMeasurementType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
