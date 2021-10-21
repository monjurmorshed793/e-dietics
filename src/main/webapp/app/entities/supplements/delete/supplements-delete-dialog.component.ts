import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISupplements } from '../supplements.model';
import { SupplementsService } from '../service/supplements.service';

@Component({
  templateUrl: './supplements-delete-dialog.component.html',
})
export class SupplementsDeleteDialogComponent {
  supplements?: ISupplements;

  constructor(protected supplementsService: SupplementsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.supplementsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
