import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PatientBiochemicalTestComponent } from './list/patient-biochemical-test.component';
import { PatientBiochemicalTestDetailComponent } from './detail/patient-biochemical-test-detail.component';
import { PatientBiochemicalTestUpdateComponent } from './update/patient-biochemical-test-update.component';
import { PatientBiochemicalTestDeleteDialogComponent } from './delete/patient-biochemical-test-delete-dialog.component';
import { PatientBiochemicalTestRoutingModule } from './route/patient-biochemical-test-routing.module';

@NgModule({
  imports: [SharedModule, PatientBiochemicalTestRoutingModule],
  declarations: [
    PatientBiochemicalTestComponent,
    PatientBiochemicalTestDetailComponent,
    PatientBiochemicalTestUpdateComponent,
    PatientBiochemicalTestDeleteDialogComponent,
  ],
  entryComponents: [PatientBiochemicalTestDeleteDialogComponent],
})
export class PatientBiochemicalTestModule {}
