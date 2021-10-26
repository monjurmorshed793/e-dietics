import { Component, OnInit } from '@angular/core';
import { ComponentNavigationService } from '../../service/component-navigation.service';
import { ComponentNavigation, IComponentNavigation } from '../../component-navigation.model';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  componentNavigations: IComponentNavigation[] = [];

  constructor(private componentNavigationService: ComponentNavigationService) {}

  ngOnInit(): void {
    this.componentNavigationService.query().subscribe(res => {
      this.componentNavigations = res.body!;
    });
  }
}
