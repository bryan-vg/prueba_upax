import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GenderComponent } from './list/gender.component';
import { GenderDetailComponent } from './detail/gender-detail.component';
import { GenderUpdateComponent } from './update/gender-update.component';
import { GenderDeleteDialogComponent } from './delete/gender-delete-dialog.component';
import { GenderRoutingModule } from './route/gender-routing.module';

@NgModule({
  imports: [SharedModule, GenderRoutingModule],
  declarations: [GenderComponent, GenderDetailComponent, GenderUpdateComponent, GenderDeleteDialogComponent],
  entryComponents: [GenderDeleteDialogComponent],
})
export class GenderModule {}
