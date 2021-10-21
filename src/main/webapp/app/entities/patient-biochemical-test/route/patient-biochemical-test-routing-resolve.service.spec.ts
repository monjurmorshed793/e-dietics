jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPatientBiochemicalTest, PatientBiochemicalTest } from '../patient-biochemical-test.model';
import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';

import { PatientBiochemicalTestRoutingResolveService } from './patient-biochemical-test-routing-resolve.service';

describe('PatientBiochemicalTest routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PatientBiochemicalTestRoutingResolveService;
  let service: PatientBiochemicalTestService;
  let resultPatientBiochemicalTest: IPatientBiochemicalTest | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PatientBiochemicalTestRoutingResolveService);
    service = TestBed.inject(PatientBiochemicalTestService);
    resultPatientBiochemicalTest = undefined;
  });

  describe('resolve', () => {
    it('should return IPatientBiochemicalTest returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientBiochemicalTest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultPatientBiochemicalTest).toEqual({ id: 'ABC' });
    });

    it('should return new IPatientBiochemicalTest if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientBiochemicalTest = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPatientBiochemicalTest).toEqual(new PatientBiochemicalTest());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PatientBiochemicalTest })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientBiochemicalTest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultPatientBiochemicalTest).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
