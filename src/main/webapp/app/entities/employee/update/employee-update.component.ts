import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IGender } from 'app/entities/gender/gender.model';
import { GenderService } from 'app/entities/gender/service/gender.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  jobsSharedCollection: IJob[] = [];
  gendersSharedCollection: IGender[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    lastName: [],
    birthDate: [],
    job: [],
    gender: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected jobService: JobService,
    protected genderService: GenderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackJobById(index: number, item: IJob): number {
    return item.id!;
  }

  trackGenderById(index: number, item: IGender): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      name: employee.name,
      lastName: employee.lastName,
      birthDate: employee.birthDate,
      job: employee.job,
      gender: employee.gender,
    });

    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing(this.jobsSharedCollection, employee.job);
    this.gendersSharedCollection = this.genderService.addGenderToCollectionIfMissing(this.gendersSharedCollection, employee.gender);
  }

  protected loadRelationshipsOptions(): void {
    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing(jobs, this.editForm.get('job')!.value)))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));

    this.genderService
      .query()
      .pipe(map((res: HttpResponse<IGender[]>) => res.body ?? []))
      .pipe(map((genders: IGender[]) => this.genderService.addGenderToCollectionIfMissing(genders, this.editForm.get('gender')!.value)))
      .subscribe((genders: IGender[]) => (this.gendersSharedCollection = genders));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      job: this.editForm.get(['job'])!.value,
      gender: this.editForm.get(['gender'])!.value,
    };
  }
}
