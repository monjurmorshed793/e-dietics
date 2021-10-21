import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFoodCategory } from '../food-category.model';
import { FoodCategoryService } from '../service/food-category.service';

@Component({
  templateUrl: './food-category-delete-dialog.component.html',
})
export class FoodCategoryDeleteDialogComponent {
  foodCategory?: IFoodCategory;

  constructor(protected foodCategoryService: FoodCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.foodCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
