import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DietNatureComponent } from './list/diet-nature.component';
import { DietNatureDetailComponent } from './detail/diet-nature-detail.component';
import { DietNatureUpdateComponent } from './update/diet-nature-update.component';
import { DietNatureDeleteDialogComponent } from './delete/diet-nature-delete-dialog.component';
import { DietNatureRoutingModule } from './route/diet-nature-routing.module';

@NgModule({
  imports: [SharedModule, DietNatureRoutingModule],
  declarations: [DietNatureComponent, DietNatureDetailComponent, DietNatureUpdateComponent, DietNatureDeleteDialogComponent],
  entryComponents: [DietNatureDeleteDialogComponent],
})
export class DietNatureModule {}
