import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NutritionStateComponent } from './list/nutrition-state.component';
import { NutritionStateDetailComponent } from './detail/nutrition-state-detail.component';
import { NutritionStateUpdateComponent } from './update/nutrition-state-update.component';
import { NutritionStateDeleteDialogComponent } from './delete/nutrition-state-delete-dialog.component';
import { NutritionStateRoutingModule } from './route/nutrition-state-routing.module';

@NgModule({
  imports: [SharedModule, NutritionStateRoutingModule],
  declarations: [
    NutritionStateComponent,
    NutritionStateDetailComponent,
    NutritionStateUpdateComponent,
    NutritionStateDeleteDialogComponent,
  ],
  entryComponents: [NutritionStateDeleteDialogComponent],
})
export class NutritionStateModule {}
