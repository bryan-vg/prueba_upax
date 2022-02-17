import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGender } from '../gender.model';
import { GenderService } from '../service/gender.service';
import { GenderDeleteDialogComponent } from '../delete/gender-delete-dialog.component';

@Component({
  selector: 'jhi-gender',
  templateUrl: './gender.component.html',
})
export class GenderComponent implements OnInit {
  genders?: IGender[];
  isLoading = false;

  constructor(protected genderService: GenderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.genderService.query().subscribe({
      next: (res: HttpResponse<IGender[]>) => {
        this.isLoading = false;
        this.genders = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGender): number {
    return item.id!;
  }

  delete(gender: IGender): void {
    const modalRef = this.modalService.open(GenderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.gender = gender;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
