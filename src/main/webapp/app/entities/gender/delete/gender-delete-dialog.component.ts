import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGender } from '../gender.model';
import { GenderService } from '../service/gender.service';

@Component({
  templateUrl: './gender-delete-dialog.component.html',
})
export class GenderDeleteDialogComponent {
  gender?: IGender;

  constructor(protected genderService: GenderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.genderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
