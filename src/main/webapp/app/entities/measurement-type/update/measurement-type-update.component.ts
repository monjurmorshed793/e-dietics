import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMeasurementType, MeasurementType } from '../measurement-type.model';
import { MeasurementTypeService } from '../service/measurement-type.service';

@Component({
  selector: 'jhi-measurement-type-update',
  templateUrl: './measurement-type-update.component.html',
})
export class MeasurementTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    label: [null, [Validators.required]],
    description: [],
  });

  constructor(
    protected measurementTypeService: MeasurementTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measurementType }) => {
      this.updateForm(measurementType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const measurementType = this.createFromForm();
    if (measurementType.id !== undefined) {
      this.subscribeToSaveResponse(this.measurementTypeService.update(measurementType));
    } else {
      this.subscribeToSaveResponse(this.measurementTypeService.create(measurementType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasurementType>>): void {
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

  protected updateForm(measurementType: IMeasurementType): void {
    this.editForm.patchValue({
      id: measurementType.id,
      label: measurementType.label,
      description: measurementType.description,
    });
  }

  protected createFromForm(): IMeasurementType {
    return {
      ...new MeasurementType(),
      id: this.editForm.get(['id'])!.value,
      label: this.editForm.get(['label'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
