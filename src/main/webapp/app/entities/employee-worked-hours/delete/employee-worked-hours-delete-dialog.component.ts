import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeWorkedHours } from '../employee-worked-hours.model';
import { EmployeeWorkedHoursService } from '../service/employee-worked-hours.service';

@Component({
  templateUrl: './employee-worked-hours-delete-dialog.component.html',
})
export class EmployeeWorkedHoursDeleteDialogComponent {
  employeeWorkedHours?: IEmployeeWorkedHours;

  constructor(protected employeeWorkedHoursService: EmployeeWorkedHoursService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeWorkedHoursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
