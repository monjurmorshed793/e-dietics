import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComponentNavigation } from '../component-navigation.model';
import { ComponentNavigationService } from '../service/component-navigation.service';

@Component({
  templateUrl: './component-navigation-delete-dialog.component.html',
})
export class ComponentNavigationDeleteDialogComponent {
  componentNavigation?: IComponentNavigation;

  constructor(protected componentNavigationService: ComponentNavigationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.componentNavigationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
