import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeWorkedHours, getEmployeeWorkedHoursIdentifier } from '../employee-worked-hours.model';

export type EntityResponseType = HttpResponse<IEmployeeWorkedHours>;
export type EntityArrayResponseType = HttpResponse<IEmployeeWorkedHours[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeWorkedHoursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-worked-hours');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeWorkedHours: IEmployeeWorkedHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeWorkedHours);
    return this.http
      .post<IEmployeeWorkedHours>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employeeWorkedHours: IEmployeeWorkedHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeWorkedHours);
    return this.http
      .put<IEmployeeWorkedHours>(`${this.resourceUrl}/${getEmployeeWorkedHoursIdentifier(employeeWorkedHours) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(employeeWorkedHours: IEmployeeWorkedHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeWorkedHours);
    return this.http
      .patch<IEmployeeWorkedHours>(`${this.resourceUrl}/${getEmployeeWorkedHoursIdentifier(employeeWorkedHours) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployeeWorkedHours>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeWorkedHours[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmployeeWorkedHoursToCollectionIfMissing(
    employeeWorkedHoursCollection: IEmployeeWorkedHours[],
    ...employeeWorkedHoursToCheck: (IEmployeeWorkedHours | null | undefined)[]
  ): IEmployeeWorkedHours[] {
    const employeeWorkedHours: IEmployeeWorkedHours[] = employeeWorkedHoursToCheck.filter(isPresent);
    if (employeeWorkedHours.length > 0) {
      const employeeWorkedHoursCollectionIdentifiers = employeeWorkedHoursCollection.map(
        employeeWorkedHoursItem => getEmployeeWorkedHoursIdentifier(employeeWorkedHoursItem)!
      );
      const employeeWorkedHoursToAdd = employeeWorkedHours.filter(employeeWorkedHoursItem => {
        const employeeWorkedHoursIdentifier = getEmployeeWorkedHoursIdentifier(employeeWorkedHoursItem);
        if (employeeWorkedHoursIdentifier == null || employeeWorkedHoursCollectionIdentifiers.includes(employeeWorkedHoursIdentifier)) {
          return false;
        }
        employeeWorkedHoursCollectionIdentifiers.push(employeeWorkedHoursIdentifier);
        return true;
      });
      return [...employeeWorkedHoursToAdd, ...employeeWorkedHoursCollection];
    }
    return employeeWorkedHoursCollection;
  }

  protected convertDateFromClient(employeeWorkedHours: IEmployeeWorkedHours): IEmployeeWorkedHours {
    return Object.assign({}, employeeWorkedHours, {
      workedDate: employeeWorkedHours.workedDate?.isValid() ? employeeWorkedHours.workedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.workedDate = res.body.workedDate ? dayjs(res.body.workedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employeeWorkedHours: IEmployeeWorkedHours) => {
        employeeWorkedHours.workedDate = employeeWorkedHours.workedDate ? dayjs(employeeWorkedHours.workedDate) : undefined;
      });
    }
    return res;
  }
}
