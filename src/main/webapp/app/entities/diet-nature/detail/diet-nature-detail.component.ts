import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDietNature } from '../diet-nature.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-diet-nature-detail',
  templateUrl: './diet-nature-detail.component.html',
})
export class DietNatureDetailComponent implements OnInit {
  dietNature: IDietNature | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dietNature }) => {
      this.dietNature = dietNature;
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
