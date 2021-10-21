import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BiochemicalTestComponent } from './list/biochemical-test.component';
import { BiochemicalTestDetailComponent } from './detail/biochemical-test-detail.component';
import { BiochemicalTestUpdateComponent } from './update/biochemical-test-update.component';
import { BiochemicalTestDeleteDialogComponent } from './delete/biochemical-test-delete-dialog.component';
import { BiochemicalTestRoutingModule } from './route/biochemical-test-routing.module';

@NgModule({
  imports: [SharedModule, BiochemicalTestRoutingModule],
  declarations: [
    BiochemicalTestComponent,
    BiochemicalTestDetailComponent,
    BiochemicalTestUpdateComponent,
    BiochemicalTestDeleteDialogComponent,
  ],
  entryComponents: [BiochemicalTestDeleteDialogComponent],
})
export class BiochemicalTestModule {}
