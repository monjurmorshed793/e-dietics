import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IActivityLevel, ActivityLevel } from '../activity-level.model';
import { ActivityLevelService } from '../service/activity-level.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-activity-level-update',
  templateUrl: './activity-level-update.component.html',
})
export class ActivityLevelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    order: [],
    label: [null, [Validators.required]],
    note: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected activityLevelService: ActivityLevelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activityLevel }) => {
      this.updateForm(activityLevel);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('edieticsApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activityLevel = this.createFromForm();
    if (activityLevel.id !== undefined) {
      this.subscribeToSaveResponse(this.activityLevelService.update(activityLevel));
    } else {
      this.subscribeToSaveResponse(this.activityLevelService.create(activityLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivityLevel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(activityLevel: IActivityLevel): void {
    this.editForm.patchValue({
      id: activityLevel.id,
      order: activityLevel.order,
      label: activityLevel.label,
      note: activityLevel.note,
    });
  }

  protected createFromForm(): IActivityLevel {
    return {
      ...new ActivityLevel(),
      id: this.editForm.get(['id'])!.value,
      order: this.editForm.get(['order'])!.value,
      label: this.editForm.get(['label'])!.value,
      note: this.editForm.get(['note'])!.value,
    };
  }
}
