import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComponentNavigationComponent } from './list/component-navigation.component';
import { ComponentNavigationDetailComponent } from './detail/component-navigation-detail.component';
import { ComponentNavigationUpdateComponent } from './update/component-navigation-update.component';
import { ComponentNavigationDeleteDialogComponent } from './delete/component-navigation-delete-dialog.component';
import { ComponentNavigationRoutingModule } from './route/component-navigation-routing.module';
import { SidebarComponent } from './navigation/sidebar/sidebar.component';
import { SlideMenuModule } from 'primeng/slidemenu';

@NgModule({
  imports: [SharedModule, ComponentNavigationRoutingModule, SlideMenuModule],
  declarations: [
    ComponentNavigationComponent,
    ComponentNavigationDetailComponent,
    ComponentNavigationUpdateComponent,
    ComponentNavigationDeleteDialogComponent,
    SidebarComponent,
  ],
  entryComponents: [ComponentNavigationDeleteDialogComponent],
})
export class ComponentNavigationModule {}
