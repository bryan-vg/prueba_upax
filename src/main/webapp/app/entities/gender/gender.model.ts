import { IEmployee } from 'app/entities/employee/employee.model';

export interface IGender {
  id?: number;
  name?: string | null;
  employees?: IEmployee[] | null;
}

export class Gender implements IGender {
  constructor(public id?: number, public name?: string | null, public employees?: IEmployee[] | null) {}
}

export function getGenderIdentifier(gender: IGender): number | undefined {
  return gender.id;
}
