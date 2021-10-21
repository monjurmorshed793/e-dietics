import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeasurementType } from '../measurement-type.model';
import { MeasurementTypeService } from '../service/measurement-type.service';

@Component({
  templateUrl: './measurement-type-delete-dialog.component.html',
})
export class MeasurementTypeDeleteDialogComponent {
  measurementType?: IMeasurementType;

  constructor(protected measurementTypeService: MeasurementTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.measurementTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
