import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivityLevel } from '../activity-level.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-activity-level-detail',
  templateUrl: './activity-level-detail.component.html',
})
export class ActivityLevelDetailComponent implements OnInit {
  activityLevel: IActivityLevel | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activityLevel }) => {
      this.activityLevel = activityLevel;
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
