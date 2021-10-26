import { Component, OnInit } from '@angular/core';
import { ComponentNavigationService } from '../../service/component-navigation.service';
import { ComponentNavigation, IComponentNavigation } from '../../component-navigation.model';
import { MenuItem } from 'primeng/api';
import { MenuItemContent } from 'primeng/menu';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  componentNavigations: IComponentNavigation[] = [];
  items: MenuItem[] = [];

  constructor(private componentNavigationService: ComponentNavigationService) {}

  ngOnInit(): void {
    this.componentNavigationService.query().subscribe(res => {
      this.componentNavigations = res.body!;
      const parents: IComponentNavigation[] = [];
      const parentMapChild = new Map();
      this.componentNavigations.forEach(n => {
        if (n.location === '#') {
          parents.push(n);
        } else {
          if (parentMapChild.has(n.parent)) {
            const child = parentMapChild.get(n.parent);
            child.push(n);
            parentMapChild.set(n.parent, child);
          } else {
            const child: IComponentNavigation[] = [];
            child.push(n);
            parentMapChild.set(n.parent, child);
          }
        }
      });
    });
  }
}
