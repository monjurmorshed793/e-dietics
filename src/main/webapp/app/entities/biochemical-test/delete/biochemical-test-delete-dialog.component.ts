import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBiochemicalTest } from '../biochemical-test.model';
import { BiochemicalTestService } from '../service/biochemical-test.service';

@Component({
  templateUrl: './biochemical-test-delete-dialog.component.html',
})
export class BiochemicalTestDeleteDialogComponent {
  biochemicalTest?: IBiochemicalTest;

  constructor(protected biochemicalTestService: BiochemicalTestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.biochemicalTestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
