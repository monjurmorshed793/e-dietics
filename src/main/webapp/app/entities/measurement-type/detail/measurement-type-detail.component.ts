import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMeasurementType } from '../measurement-type.model';

@Component({
  selector: 'jhi-measurement-type-detail',
  templateUrl: './measurement-type-detail.component.html',
})
export class MeasurementTypeDetailComponent implements OnInit {
  measurementType: IMeasurementType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measurementType }) => {
      this.measurementType = measurementType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
