import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';
import { IEmployeeWorkedHours, EmployeeWorkedHours } from '../employee-worked-hours.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { EmployeeWorkedHoursUpdateComponent } from './employee-worked-hours-update.component';

describe('EmployeeWorkedHours Management Update Component', () => {
  let comp: EmployeeWorkedHoursUpdateComponent;
  let fixture: ComponentFixture<EmployeeWorkedHoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeWorkedHoursService: EmployeeWorkedHoursService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeWorkedHoursUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeWorkedHoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeWorkedHoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeWorkedHoursService = TestBed.inject(EmployeeWorkedHoursService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeWorkedHours: IEmployeeWorkedHours = { id: 456 };
      const employee: IEmployee = { id: 97127 };
      employeeWorkedHours.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 78847 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeWorkedHours });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeWorkedHours: IEmployeeWorkedHours = { id: 456 };
      const employee: IEmployee = { id: 92099 };
      employeeWorkedHours.employee = employee;

      activatedRoute.data = of({ employeeWorkedHours });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employeeWorkedHours));
      expect(comp.employeesSharedCollection).toContain(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeWorkedHours>>();
      const employeeWorkedHours = { id: 123 };
      jest.spyOn(employeeWorkedHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeWorkedHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeWorkedHours }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeWorkedHoursService.update).toHaveBeenCalledWith(employeeWorkedHours);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeWorkedHours>>();
      const employeeWorkedHours = new EmployeeWorkedHours();
      jest.spyOn(employeeWorkedHoursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeWorkedHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeWorkedHours }));
      saveSubject.complete();

      // THEN
      expect(employeeWorkedHoursService.create).toHaveBeenCalledWith(employeeWorkedHours);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeWorkedHours>>();
      const employeeWorkedHours = { id: 123 };
      jest.spyOn(employeeWorkedHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeWorkedHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeWorkedHoursService.update).toHaveBeenCalledWith(employeeWorkedHours);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
