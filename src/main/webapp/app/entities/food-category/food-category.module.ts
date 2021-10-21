import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FoodCategoryComponent } from './list/food-category.component';
import { FoodCategoryDetailComponent } from './detail/food-category-detail.component';
import { FoodCategoryUpdateComponent } from './update/food-category-update.component';
import { FoodCategoryDeleteDialogComponent } from './delete/food-category-delete-dialog.component';
import { FoodCategoryRoutingModule } from './route/food-category-routing.module';

@NgModule({
  imports: [SharedModule, FoodCategoryRoutingModule],
  declarations: [FoodCategoryComponent, FoodCategoryDetailComponent, FoodCategoryUpdateComponent, FoodCategoryDeleteDialogComponent],
  entryComponents: [FoodCategoryDeleteDialogComponent],
})
export class FoodCategoryModule {}
