import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployeeWorkedHours, EmployeeWorkedHours } from '../employee-worked-hours.model';
import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-employee-worked-hours-update',
  templateUrl: './employee-worked-hours-update.component.html',
})
export class EmployeeWorkedHoursUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    workedHours: [],
    workedDate: [],
    employee: [],
  });

  constructor(
    protected employeeWorkedHoursService: EmployeeWorkedHoursService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeWorkedHours }) => {
      this.updateForm(employeeWorkedHours);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeWorkedHours = this.createFromForm();
    if (employeeWorkedHours.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeWorkedHoursService.update(employeeWorkedHours));
    } else {
      this.subscribeToSaveResponse(this.employeeWorkedHoursService.create(employeeWorkedHours));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeWorkedHours>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employeeWorkedHours: IEmployeeWorkedHours): void {
    this.editForm.patchValue({
      id: employeeWorkedHours.id,
      workedHours: employeeWorkedHours.workedHours,
      workedDate: employeeWorkedHours.workedDate,
      employee: employeeWorkedHours.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      employeeWorkedHours.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IEmployeeWorkedHours {
    return {
      ...new EmployeeWorkedHours(),
      id: this.editForm.get(['id'])!.value,
      workedHours: this.editForm.get(['workedHours'])!.value,
      workedDate: this.editForm.get(['workedDate'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
