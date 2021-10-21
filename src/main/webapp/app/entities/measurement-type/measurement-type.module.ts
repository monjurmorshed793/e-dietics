import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeasurementTypeComponent } from './list/measurement-type.component';
import { MeasurementTypeDetailComponent } from './detail/measurement-type-detail.component';
import { MeasurementTypeUpdateComponent } from './update/measurement-type-update.component';
import { MeasurementTypeDeleteDialogComponent } from './delete/measurement-type-delete-dialog.component';
import { MeasurementTypeRoutingModule } from './route/measurement-type-routing.module';

@NgModule({
  imports: [SharedModule, MeasurementTypeRoutingModule],
  declarations: [
    MeasurementTypeComponent,
    MeasurementTypeDetailComponent,
    MeasurementTypeUpdateComponent,
    MeasurementTypeDeleteDialogComponent,
  ],
  entryComponents: [MeasurementTypeDeleteDialogComponent],
})
export class MeasurementTypeModule {}
