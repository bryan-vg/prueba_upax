import { IEmployee } from 'app/entities/employee/employee.model';

export interface IJob {
  id?: number;
  name?: string | null;
  salary?: number | null;
  employees?: IEmployee[] | null;
}

export class Job implements IJob {
  constructor(public id?: number, public name?: string | null, public salary?: number | null, public employees?: IEmployee[] | null) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
