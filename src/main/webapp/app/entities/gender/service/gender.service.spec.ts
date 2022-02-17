import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGender, Gender } from '../gender.model';

import { GenderService } from './gender.service';

describe('Gender Service', () => {
  let service: GenderService;
  let httpMock: HttpTestingController;
  let elemDefault: IGender;
  let expectedResult: IGender | IGender[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GenderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Gender', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Gender()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gender', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gender', () => {
      const patchObject = Object.assign({}, new Gender());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gender', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Gender', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGenderToCollectionIfMissing', () => {
      it('should add a Gender to an empty array', () => {
        const gender: IGender = { id: 123 };
        expectedResult = service.addGenderToCollectionIfMissing([], gender);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gender);
      });

      it('should not add a Gender to an array that contains it', () => {
        const gender: IGender = { id: 123 };
        const genderCollection: IGender[] = [
          {
            ...gender,
          },
          { id: 456 },
        ];
        expectedResult = service.addGenderToCollectionIfMissing(genderCollection, gender);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gender to an array that doesn't contain it", () => {
        const gender: IGender = { id: 123 };
        const genderCollection: IGender[] = [{ id: 456 }];
        expectedResult = service.addGenderToCollectionIfMissing(genderCollection, gender);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gender);
      });

      it('should add only unique Gender to an array', () => {
        const genderArray: IGender[] = [{ id: 123 }, { id: 456 }, { id: 89830 }];
        const genderCollection: IGender[] = [{ id: 123 }];
        expectedResult = service.addGenderToCollectionIfMissing(genderCollection, ...genderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gender: IGender = { id: 123 };
        const gender2: IGender = { id: 456 };
        expectedResult = service.addGenderToCollectionIfMissing([], gender, gender2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gender);
        expect(expectedResult).toContain(gender2);
      });

      it('should accept null and undefined values', () => {
        const gender: IGender = { id: 123 };
        expectedResult = service.addGenderToCollectionIfMissing([], null, gender, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gender);
      });

      it('should return initial array if no Gender is added', () => {
        const genderCollection: IGender[] = [{ id: 123 }];
        expectedResult = service.addGenderToCollectionIfMissing(genderCollection, undefined, null);
        expect(expectedResult).toEqual(genderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
