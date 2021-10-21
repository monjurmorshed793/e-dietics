jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IActivityLevel, ActivityLevel } from '../activity-level.model';
import { ActivityLevelService } from '../service/activity-level.service';

import { ActivityLevelRoutingResolveService } from './activity-level-routing-resolve.service';

describe('ActivityLevel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ActivityLevelRoutingResolveService;
  let service: ActivityLevelService;
  let resultActivityLevel: IActivityLevel | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ActivityLevelRoutingResolveService);
    service = TestBed.inject(ActivityLevelService);
    resultActivityLevel = undefined;
  });

  describe('resolve', () => {
    it('should return IActivityLevel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivityLevel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultActivityLevel).toEqual({ id: 'ABC' });
    });

    it('should return new IActivityLevel if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivityLevel = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultActivityLevel).toEqual(new ActivityLevel());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ActivityLevel })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultActivityLevel = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultActivityLevel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
