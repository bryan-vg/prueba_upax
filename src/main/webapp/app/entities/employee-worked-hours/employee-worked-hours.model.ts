import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEmployeeWorkedHours {
  id?: number;
  workedHours?: number | null;
  workedDate?: dayjs.Dayjs | null;
  employee?: IEmployee | null;
}

export class EmployeeWorkedHours implements IEmployeeWorkedHours {
  constructor(
    public id?: number,
    public workedHours?: number | null,
    public workedDate?: dayjs.Dayjs | null,
    public employee?: IEmployee | null
  ) {}
}

export function getEmployeeWorkedHoursIdentifier(employeeWorkedHours: IEmployeeWorkedHours): number | undefined {
  return employeeWorkedHours.id;
}
