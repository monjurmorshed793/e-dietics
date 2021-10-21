import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActivityLevelComponent } from './list/activity-level.component';
import { ActivityLevelDetailComponent } from './detail/activity-level-detail.component';
import { ActivityLevelUpdateComponent } from './update/activity-level-update.component';
import { ActivityLevelDeleteDialogComponent } from './delete/activity-level-delete-dialog.component';
import { ActivityLevelRoutingModule } from './route/activity-level-routing.module';

@NgModule({
  imports: [SharedModule, ActivityLevelRoutingModule],
  declarations: [ActivityLevelComponent, ActivityLevelDetailComponent, ActivityLevelUpdateComponent, ActivityLevelDeleteDialogComponent],
  entryComponents: [ActivityLevelDeleteDialogComponent],
})
export class ActivityLevelModule {}
