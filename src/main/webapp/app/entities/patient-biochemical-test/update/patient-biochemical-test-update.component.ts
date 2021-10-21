import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPatientBiochemicalTest, PatientBiochemicalTest } from '../patient-biochemical-test.model';
import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IBiochemicalTest } from 'app/entities/biochemical-test/biochemical-test.model';
import { BiochemicalTestService } from 'app/entities/biochemical-test/service/biochemical-test.service';
import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';
import { MeasurementTypeService } from 'app/entities/measurement-type/service/measurement-type.service';

@Component({
  selector: 'jhi-patient-biochemical-test-update',
  templateUrl: './patient-biochemical-test-update.component.html',
})
export class PatientBiochemicalTestUpdateComponent implements OnInit {
  isSaving = false;

  patientsSharedCollection: IPatient[] = [];
  biochemicalTestsSharedCollection: IBiochemicalTest[] = [];
  measurementTypesSharedCollection: IMeasurementType[] = [];

  editForm = this.fb.group({
    id: [],
    other: [],
    value: [],
    patient: [],
    biochemicalTest: [],
    measurementType: [],
  });

  constructor(
    protected patientBiochemicalTestService: PatientBiochemicalTestService,
    protected patientService: PatientService,
    protected biochemicalTestService: BiochemicalTestService,
    protected measurementTypeService: MeasurementTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientBiochemicalTest }) => {
      this.updateForm(patientBiochemicalTest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patientBiochemicalTest = this.createFromForm();
    if (patientBiochemicalTest.id !== undefined) {
      this.subscribeToSaveResponse(this.patientBiochemicalTestService.update(patientBiochemicalTest));
    } else {
      this.subscribeToSaveResponse(this.patientBiochemicalTestService.create(patientBiochemicalTest));
    }
  }

  trackPatientById(index: number, item: IPatient): string {
    return item.id!;
  }

  trackBiochemicalTestById(index: number, item: IBiochemicalTest): string {
    return item.id!;
  }

  trackMeasurementTypeById(index: number, item: IMeasurementType): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatientBiochemicalTest>>): void {
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

  protected updateForm(patientBiochemicalTest: IPatientBiochemicalTest): void {
    this.editForm.patchValue({
      id: patientBiochemicalTest.id,
      other: patientBiochemicalTest.other,
      value: patientBiochemicalTest.value,
      patient: patientBiochemicalTest.patient,
      biochemicalTest: patientBiochemicalTest.biochemicalTest,
      measurementType: patientBiochemicalTest.measurementType,
    });

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(
      this.patientsSharedCollection,
      patientBiochemicalTest.patient
    );
    this.biochemicalTestsSharedCollection = this.biochemicalTestService.addBiochemicalTestToCollectionIfMissing(
      this.biochemicalTestsSharedCollection,
      patientBiochemicalTest.biochemicalTest
    );
    this.measurementTypesSharedCollection = this.measurementTypeService.addMeasurementTypeToCollectionIfMissing(
      this.measurementTypesSharedCollection,
      patientBiochemicalTest.measurementType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing(patients, this.editForm.get('patient')!.value))
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));

    this.biochemicalTestService
      .query()
      .pipe(map((res: HttpResponse<IBiochemicalTest[]>) => res.body ?? []))
      .pipe(
        map((biochemicalTests: IBiochemicalTest[]) =>
          this.biochemicalTestService.addBiochemicalTestToCollectionIfMissing(biochemicalTests, this.editForm.get('biochemicalTest')!.value)
        )
      )
      .subscribe((biochemicalTests: IBiochemicalTest[]) => (this.biochemicalTestsSharedCollection = biochemicalTests));

    this.measurementTypeService
      .query()
      .pipe(map((res: HttpResponse<IMeasurementType[]>) => res.body ?? []))
      .pipe(
        map((measurementTypes: IMeasurementType[]) =>
          this.measurementTypeService.addMeasurementTypeToCollectionIfMissing(measurementTypes, this.editForm.get('measurementType')!.value)
        )
      )
      .subscribe((measurementTypes: IMeasurementType[]) => (this.measurementTypesSharedCollection = measurementTypes));
  }

  protected createFromForm(): IPatientBiochemicalTest {
    return {
      ...new PatientBiochemicalTest(),
      id: this.editForm.get(['id'])!.value,
      other: this.editForm.get(['other'])!.value,
      value: this.editForm.get(['value'])!.value,
      patient: this.editForm.get(['patient'])!.value,
      biochemicalTest: this.editForm.get(['biochemicalTest'])!.value,
      measurementType: this.editForm.get(['measurementType'])!.value,
    };
  }
}
