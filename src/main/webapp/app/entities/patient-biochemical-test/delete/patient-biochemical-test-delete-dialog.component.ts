import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatientBiochemicalTest } from '../patient-biochemical-test.model';
import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';

@Component({
  templateUrl: './patient-biochemical-test-delete-dialog.component.html',
})
export class PatientBiochemicalTestDeleteDialogComponent {
  patientBiochemicalTest?: IPatientBiochemicalTest;

  constructor(protected patientBiochemicalTestService: PatientBiochemicalTestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.patientBiochemicalTestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
