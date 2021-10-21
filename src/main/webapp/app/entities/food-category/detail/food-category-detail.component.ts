import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFoodCategory } from '../food-category.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-food-category-detail',
  templateUrl: './food-category-detail.component.html',
})
export class FoodCategoryDetailComponent implements OnInit {
  foodCategory: IFoodCategory | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodCategory }) => {
      this.foodCategory = foodCategory;
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
