import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEmployeeWorkedHours, EmployeeWorkedHours } from '../employee-worked-hours.model';
import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';

import { EmployeeWorkedHoursRoutingResolveService } from './employee-worked-hours-routing-resolve.service';

describe('EmployeeWorkedHours routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmployeeWorkedHoursRoutingResolveService;
  let service: EmployeeWorkedHoursService;
  let resultEmployeeWorkedHours: IEmployeeWorkedHours | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(EmployeeWorkedHoursRoutingResolveService);
    service = TestBed.inject(EmployeeWorkedHoursService);
    resultEmployeeWorkedHours = undefined;
  });

  describe('resolve', () => {
    it('should return IEmployeeWorkedHours returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeWorkedHours = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeWorkedHours).toEqual({ id: 123 });
    });

    it('should return new IEmployeeWorkedHours if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeWorkedHours = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmployeeWorkedHours).toEqual(new EmployeeWorkedHours());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmployeeWorkedHours })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmployeeWorkedHours = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmployeeWorkedHours).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
