jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISupplements, Supplements } from '../supplements.model';
import { SupplementsService } from '../service/supplements.service';

import { SupplementsRoutingResolveService } from './supplements-routing-resolve.service';

describe('Supplements routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SupplementsRoutingResolveService;
  let service: SupplementsService;
  let resultSupplements: ISupplements | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SupplementsRoutingResolveService);
    service = TestBed.inject(SupplementsService);
    resultSupplements = undefined;
  });

  describe('resolve', () => {
    it('should return ISupplements returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSupplements = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultSupplements).toEqual({ id: 'ABC' });
    });

    it('should return new ISupplements if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSupplements = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSupplements).toEqual(new Supplements());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Supplements })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSupplements = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultSupplements).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
