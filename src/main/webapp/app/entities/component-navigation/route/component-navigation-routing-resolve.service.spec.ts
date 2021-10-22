jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IComponentNavigation, ComponentNavigation } from '../component-navigation.model';
import { ComponentNavigationService } from '../service/component-navigation.service';

import { ComponentNavigationRoutingResolveService } from './component-navigation-routing-resolve.service';

describe('ComponentNavigation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ComponentNavigationRoutingResolveService;
  let service: ComponentNavigationService;
  let resultComponentNavigation: IComponentNavigation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ComponentNavigationRoutingResolveService);
    service = TestBed.inject(ComponentNavigationService);
    resultComponentNavigation = undefined;
  });

  describe('resolve', () => {
    it('should return IComponentNavigation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComponentNavigation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultComponentNavigation).toEqual({ id: 'ABC' });
    });

    it('should return new IComponentNavigation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComponentNavigation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultComponentNavigation).toEqual(new ComponentNavigation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ComponentNavigation })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComponentNavigation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultComponentNavigation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
