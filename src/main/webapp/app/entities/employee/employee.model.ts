import dayjs from 'dayjs/esm';
import { IEmployeeWorkedHours } from 'app/entities/employee-worked-hours/employee-worked-hours.model';
import { IJob } from 'app/entities/job/job.model';
import { IGender } from 'app/entities/gender/gender.model';

export interface IEmployee {
  id?: number;
  name?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  employeeWorkedHours?: IEmployeeWorkedHours[] | null;
  job?: IJob | null;
  gender?: IGender | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public name?: string | null,
    public lastName?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public employeeWorkedHours?: IEmployeeWorkedHours[] | null,
    public job?: IJob | null,
    public gender?: IGender | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
