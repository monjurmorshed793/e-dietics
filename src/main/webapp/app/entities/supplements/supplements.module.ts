import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SupplementsComponent } from './list/supplements.component';
import { SupplementsDetailComponent } from './detail/supplements-detail.component';
import { SupplementsUpdateComponent } from './update/supplements-update.component';
import { SupplementsDeleteDialogComponent } from './delete/supplements-delete-dialog.component';
import { SupplementsRoutingModule } from './route/supplements-routing.module';

@NgModule({
  imports: [SharedModule, SupplementsRoutingModule],
  declarations: [SupplementsComponent, SupplementsDetailComponent, SupplementsUpdateComponent, SupplementsDeleteDialogComponent],
  entryComponents: [SupplementsDeleteDialogComponent],
})
export class SupplementsModule {}
