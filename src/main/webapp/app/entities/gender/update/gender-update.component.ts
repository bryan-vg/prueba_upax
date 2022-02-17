import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGender, Gender } from '../gender.model';
import { GenderService } from '../service/gender.service';

@Component({
  selector: 'jhi-gender-update',
  templateUrl: './gender-update.component.html',
})
export class GenderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected genderService: GenderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gender }) => {
      this.updateForm(gender);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gender = this.createFromForm();
    if (gender.id !== undefined) {
      this.subscribeToSaveResponse(this.genderService.update(gender));
    } else {
      this.subscribeToSaveResponse(this.genderService.create(gender));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGender>>): void {
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

  protected updateForm(gender: IGender): void {
    this.editForm.patchValue({
      id: gender.id,
      name: gender.name,
    });
  }

  protected createFromForm(): IGender {
    return {
      ...new Gender(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
