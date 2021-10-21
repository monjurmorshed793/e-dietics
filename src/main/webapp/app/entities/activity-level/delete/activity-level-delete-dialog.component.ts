import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivityLevel } from '../activity-level.model';
import { ActivityLevelService } from '../service/activity-level.service';

@Component({
  templateUrl: './activity-level-delete-dialog.component.html',
})
export class ActivityLevelDeleteDialogComponent {
  activityLevel?: IActivityLevel;

  constructor(protected activityLevelService: ActivityLevelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.activityLevelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
