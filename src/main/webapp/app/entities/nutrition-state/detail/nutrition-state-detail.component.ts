import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INutritionState } from '../nutrition-state.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-nutrition-state-detail',
  templateUrl: './nutrition-state-detail.component.html',
})
export class NutritionStateDetailComponent implements OnInit {
  nutritionState: INutritionState | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nutritionState }) => {
      this.nutritionState = nutritionState;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
