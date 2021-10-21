import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBiochemicalTest, BiochemicalTest } from '../biochemical-test.model';
import { BiochemicalTestService } from '../service/biochemical-test.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';
import { MeasurementTypeService } from 'app/entities/measurement-type/service/measurement-type.service';

@Component({
  selector: 'jhi-biochemical-test-update',
  templateUrl: './biochemical-test-update.component.html',
})
export class BiochemicalTestUpdateComponent implements OnInit {
  isSaving = false;

  measurementTypesSharedCollection: IMeasurementType[] = [];

  editForm = this.fb.group({
    id: [],
    testName: [],
    description: [],
    defaultMeasurementType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected biochemicalTestService: BiochemicalTestService,
    protected measurementTypeService: MeasurementTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ biochemicalTest }) => {
      this.updateForm(biochemicalTest);

      this.loadRelationshipsOptions();
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
    const biochemicalTest = this.createFromForm();
    if (biochemicalTest.id !== undefined) {
      this.subscribeToSaveResponse(this.biochemicalTestService.update(biochemicalTest));
    } else {
      this.subscribeToSaveResponse(this.biochemicalTestService.create(biochemicalTest));
    }
  }

  trackMeasurementTypeById(index: number, item: IMeasurementType): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBiochemicalTest>>): void {
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

  protected updateForm(biochemicalTest: IBiochemicalTest): void {
    this.editForm.patchValue({
      id: biochemicalTest.id,
      testName: biochemicalTest.testName,
      description: biochemicalTest.description,
      defaultMeasurementType: biochemicalTest.defaultMeasurementType,
    });

    this.measurementTypesSharedCollection = this.measurementTypeService.addMeasurementTypeToCollectionIfMissing(
      this.measurementTypesSharedCollection,
      biochemicalTest.defaultMeasurementType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.measurementTypeService
      .query()
      .pipe(map((res: HttpResponse<IMeasurementType[]>) => res.body ?? []))
      .pipe(
        map((measurementTypes: IMeasurementType[]) =>
          this.measurementTypeService.addMeasurementTypeToCollectionIfMissing(
            measurementTypes,
            this.editForm.get('defaultMeasurementType')!.value
          )
        )
      )
      .subscribe((measurementTypes: IMeasurementType[]) => (this.measurementTypesSharedCollection = measurementTypes));
  }

  protected createFromForm(): IBiochemicalTest {
    return {
      ...new BiochemicalTest(),
      id: this.editForm.get(['id'])!.value,
      testName: this.editForm.get(['testName'])!.value,
      description: this.editForm.get(['description'])!.value,
      defaultMeasurementType: this.editForm.get(['defaultMeasurementType'])!.value,
    };
  }
}
