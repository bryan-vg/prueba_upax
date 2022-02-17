import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmployeeWorkedHours, EmployeeWorkedHours } from '../employee-worked-hours.model';

import { EmployeeWorkedHoursService } from './employee-worked-hours.service';

describe('EmployeeWorkedHours Service', () => {
  let service: EmployeeWorkedHoursService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmployeeWorkedHours;
  let expectedResult: IEmployeeWorkedHours | IEmployeeWorkedHours[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeWorkedHoursService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      workedHours: 0,
      workedDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          workedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a EmployeeWorkedHours', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          workedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          workedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new EmployeeWorkedHours()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeWorkedHours', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          workedHours: 1,
          workedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          workedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeWorkedHours', () => {
      const patchObject = Object.assign(
        {
          workedHours: 1,
          workedDate: currentDate.format(DATE_FORMAT),
        },
        new EmployeeWorkedHours()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          workedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeWorkedHours', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          workedHours: 1,
          workedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          workedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a EmployeeWorkedHours', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmployeeWorkedHoursToCollectionIfMissing', () => {
      it('should add a EmployeeWorkedHours to an empty array', () => {
        const employeeWorkedHours: IEmployeeWorkedHours = { id: 123 };
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing([], employeeWorkedHours);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeWorkedHours);
      });

      it('should not add a EmployeeWorkedHours to an array that contains it', () => {
        const employeeWorkedHours: IEmployeeWorkedHours = { id: 123 };
        const employeeWorkedHoursCollection: IEmployeeWorkedHours[] = [
          {
            ...employeeWorkedHours,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing(employeeWorkedHoursCollection, employeeWorkedHours);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeWorkedHours to an array that doesn't contain it", () => {
        const employeeWorkedHours: IEmployeeWorkedHours = { id: 123 };
        const employeeWorkedHoursCollection: IEmployeeWorkedHours[] = [{ id: 456 }];
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing(employeeWorkedHoursCollection, employeeWorkedHours);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeWorkedHours);
      });

      it('should add only unique EmployeeWorkedHours to an array', () => {
        const employeeWorkedHoursArray: IEmployeeWorkedHours[] = [{ id: 123 }, { id: 456 }, { id: 25318 }];
        const employeeWorkedHoursCollection: IEmployeeWorkedHours[] = [{ id: 123 }];
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing(employeeWorkedHoursCollection, ...employeeWorkedHoursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeWorkedHours: IEmployeeWorkedHours = { id: 123 };
        const employeeWorkedHours2: IEmployeeWorkedHours = { id: 456 };
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing([], employeeWorkedHours, employeeWorkedHours2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeWorkedHours);
        expect(expectedResult).toContain(employeeWorkedHours2);
      });

      it('should accept null and undefined values', () => {
        const employeeWorkedHours: IEmployeeWorkedHours = { id: 123 };
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing([], null, employeeWorkedHours, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeWorkedHours);
      });

      it('should return initial array if no EmployeeWorkedHours is added', () => {
        const employeeWorkedHoursCollection: IEmployeeWorkedHours[] = [{ id: 123 }];
        expectedResult = service.addEmployeeWorkedHoursToCollectionIfMissing(employeeWorkedHoursCollection, undefined, null);
        expect(expectedResult).toEqual(employeeWorkedHoursCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
