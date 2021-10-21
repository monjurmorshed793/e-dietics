import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDietNature } from '../diet-nature.model';
import { DietNatureService } from '../service/diet-nature.service';

@Component({
  templateUrl: './diet-nature-delete-dialog.component.html',
})
export class DietNatureDeleteDialogComponent {
  dietNature?: IDietNature;

  constructor(protected dietNatureService: DietNatureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.dietNatureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
