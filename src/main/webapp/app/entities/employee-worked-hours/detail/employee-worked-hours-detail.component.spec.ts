import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeWorkedHoursDetailComponent } from './employee-worked-hours-detail.component';

describe('EmployeeWorkedHours Management Detail Component', () => {
  let comp: EmployeeWorkedHoursDetailComponent;
  let fixture: ComponentFixture<EmployeeWorkedHoursDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeWorkedHoursDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeWorkedHours: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeWorkedHoursDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeWorkedHoursDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeWorkedHours on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeWorkedHours).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
