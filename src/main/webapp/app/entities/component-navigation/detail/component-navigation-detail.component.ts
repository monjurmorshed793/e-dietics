import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComponentNavigation } from '../component-navigation.model';

@Component({
  selector: 'jhi-component-navigation-detail',
  templateUrl: './component-navigation-detail.component.html',
})
export class ComponentNavigationDetailComponent implements OnInit {
  componentNavigation: IComponentNavigation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ componentNavigation }) => {
      this.componentNavigation = componentNavigation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
