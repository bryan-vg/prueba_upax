import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeWorkedHours } from '../employee-worked-hours.model';
import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';
import { EmployeeWorkedHoursDeleteDialogComponent } from '../delete/employee-worked-hours-delete-dialog.component';

@Component({
  selector: 'jhi-employee-worked-hours',
  templateUrl: './employee-worked-hours.component.html',
})
export class EmployeeWorkedHoursComponent implements OnInit {
  employeeWorkedHours?: IEmployeeWorkedHours[];
  isLoading = false;

  constructor(protected employeeWorkedHoursService: EmployeeWorkedHoursService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.employeeWorkedHoursService.query().subscribe({
      next: (res: HttpResponse<IEmployeeWorkedHours[]>) => {
        this.isLoading = false;
        this.employeeWorkedHours = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEmployeeWorkedHours): number {
    return item.id!;
  }

  delete(employeeWorkedHours: IEmployeeWorkedHours): void {
    const modalRef = this.modalService.open(EmployeeWorkedHoursDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employeeWorkedHours = employeeWorkedHours;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
