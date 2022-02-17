import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeWorkedHoursComponent } from './list/employee-worked-hours.component';
import { EmployeeWorkedHoursDetailComponent } from './detail/employee-worked-hours-detail.component';
import { EmployeeWorkedHoursUpdateComponent } from './update/employee-worked-hours-update.component';
import { EmployeeWorkedHoursDeleteDialogComponent } from './delete/employee-worked-hours-delete-dialog.component';
import { EmployeeWorkedHoursRoutingModule } from './route/employee-worked-hours-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeWorkedHoursRoutingModule],
  declarations: [
    EmployeeWorkedHoursComponent,
    EmployeeWorkedHoursDetailComponent,
    EmployeeWorkedHoursUpdateComponent,
    EmployeeWorkedHoursDeleteDialogComponent,
  ],
  entryComponents: [EmployeeWorkedHoursDeleteDialogComponent],
})
export class EmployeeWorkedHoursModule {}
