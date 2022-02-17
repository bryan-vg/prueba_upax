import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';

import { EmployeeWorkedHoursComponent } from './employee-worked-hours.component';

describe('EmployeeWorkedHours Management Component', () => {
  let comp: EmployeeWorkedHoursComponent;
  let fixture: ComponentFixture<EmployeeWorkedHoursComponent>;
  let service: EmployeeWorkedHoursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmployeeWorkedHoursComponent],
    })
      .overrideTemplate(EmployeeWorkedHoursComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeWorkedHoursComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmployeeWorkedHoursService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.employeeWorkedHours?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
