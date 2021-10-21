import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INutritionState } from '../nutrition-state.model';
import { NutritionStateService } from '../service/nutrition-state.service';

@Component({
  templateUrl: './nutrition-state-delete-dialog.component.html',
})
export class NutritionStateDeleteDialogComponent {
  nutritionState?: INutritionState;

  constructor(protected nutritionStateService: NutritionStateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.nutritionStateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
