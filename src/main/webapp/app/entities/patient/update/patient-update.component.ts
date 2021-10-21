import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPatient, Patient } from '../patient.model';
import { PatientService } from '../service/patient.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { INutritionState } from 'app/entities/nutrition-state/nutrition-state.model';
import { NutritionStateService } from 'app/entities/nutrition-state/service/nutrition-state.service';
import { IActivityLevel } from 'app/entities/activity-level/activity-level.model';
import { ActivityLevelService } from 'app/entities/activity-level/service/activity-level.service';
import { IDietNature } from 'app/entities/diet-nature/diet-nature.model';
import { DietNatureService } from 'app/entities/diet-nature/service/diet-nature.service';
import { ISupplements } from 'app/entities/supplements/supplements.model';
import { SupplementsService } from 'app/entities/supplements/service/supplements.service';
import { IFoodCategory } from 'app/entities/food-category/food-category.model';
import { FoodCategoryService } from 'app/entities/food-category/service/food-category.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { WeightType } from 'app/entities/enumerations/weight-type.model';
import { HeightMeasureType } from 'app/entities/enumerations/height-measure-type.model';
import { GainLossType } from 'app/entities/enumerations/gain-loss-type.model';
import { AppetiteType } from 'app/entities/enumerations/appetite-type.model';
import { PhysicalActivityType } from 'app/entities/enumerations/physical-activity-type.model';
import { ReligionType } from 'app/entities/enumerations/religion-type.model';
import { AreaType } from 'app/entities/enumerations/area-type.model';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;
  genderValues = Object.keys(Gender);
  weightTypeValues = Object.keys(WeightType);
  heightMeasureTypeValues = Object.keys(HeightMeasureType);
  gainLossTypeValues = Object.keys(GainLossType);
  appetiteTypeValues = Object.keys(AppetiteType);
  physicalActivityTypeValues = Object.keys(PhysicalActivityType);
  religionTypeValues = Object.keys(ReligionType);
  areaTypeValues = Object.keys(AreaType);

  nutritionStatesSharedCollection: INutritionState[] = [];
  activityLevelsSharedCollection: IActivityLevel[] = [];
  dietNaturesSharedCollection: IDietNature[] = [];
  supplementsSharedCollection: ISupplements[] = [];
  foodCategoriesSharedCollection: IFoodCategory[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    address: [],
    hospital: [],
    admissionDate: [],
    reasonOfAdmission: [],
    wordNo: [],
    bedNo: [],
    healthCondition: [],
    mentalStatus: [],
    age: [],
    sex: [],
    weight: [],
    weightType: [],
    height: [],
    heightMeasureType: [],
    ibw: [],
    bmi: [],
    recentWeightGainLoss: [],
    gainLossMeasure: [],
    gainLossTimeFrame: [],
    gainLossType: [],
    supplementTaken: [],
    appetite: [],
    physicalActivity: [],
    monthlyFamilyIncome: [],
    religion: [],
    education: [],
    occupation: [],
    livingStatus: [],
    area: [],
    estimatedEnergyNeeds: [],
    calculateEnergyNeeds: [],
    totalKCal: [],
    carbohydrate: [],
    protein: [],
    fat: [],
    fluid: [],
    foodRestriction: [],
    nutritionState: [],
    activityLevel: [],
    dietNatures: [],
    supplements: [],
    restrictedFoodCategories: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected patientService: PatientService,
    protected nutritionStateService: NutritionStateService,
    protected activityLevelService: ActivityLevelService,
    protected dietNatureService: DietNatureService,
    protected supplementsService: SupplementsService,
    protected foodCategoryService: FoodCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);

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
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  trackNutritionStateById(index: number, item: INutritionState): string {
    return item.id!;
  }

  trackActivityLevelById(index: number, item: IActivityLevel): string {
    return item.id!;
  }

  trackDietNatureById(index: number, item: IDietNature): string {
    return item.id!;
  }

  trackSupplementsById(index: number, item: ISupplements): string {
    return item.id!;
  }

  trackFoodCategoryById(index: number, item: IFoodCategory): string {
    return item.id!;
  }

  getSelectedDietNature(option: IDietNature, selectedVals?: IDietNature[]): IDietNature {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedSupplements(option: ISupplements, selectedVals?: ISupplements[]): ISupplements {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedFoodCategory(option: IFoodCategory, selectedVals?: IFoodCategory[]): IFoodCategory {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  protected updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      name: patient.name,
      address: patient.address,
      hospital: patient.hospital,
      admissionDate: patient.admissionDate,
      reasonOfAdmission: patient.reasonOfAdmission,
      wordNo: patient.wordNo,
      bedNo: patient.bedNo,
      healthCondition: patient.healthCondition,
      mentalStatus: patient.mentalStatus,
      age: patient.age,
      sex: patient.sex,
      weight: patient.weight,
      weightType: patient.weightType,
      height: patient.height,
      heightMeasureType: patient.heightMeasureType,
      ibw: patient.ibw,
      bmi: patient.bmi,
      recentWeightGainLoss: patient.recentWeightGainLoss,
      gainLossMeasure: patient.gainLossMeasure,
      gainLossTimeFrame: patient.gainLossTimeFrame,
      gainLossType: patient.gainLossType,
      supplementTaken: patient.supplementTaken,
      appetite: patient.appetite,
      physicalActivity: patient.physicalActivity,
      monthlyFamilyIncome: patient.monthlyFamilyIncome,
      religion: patient.religion,
      education: patient.education,
      occupation: patient.occupation,
      livingStatus: patient.livingStatus,
      area: patient.area,
      estimatedEnergyNeeds: patient.estimatedEnergyNeeds,
      calculateEnergyNeeds: patient.calculateEnergyNeeds,
      totalKCal: patient.totalKCal,
      carbohydrate: patient.carbohydrate,
      protein: patient.protein,
      fat: patient.fat,
      fluid: patient.fluid,
      foodRestriction: patient.foodRestriction,
      nutritionState: patient.nutritionState,
      activityLevel: patient.activityLevel,
      dietNatures: patient.dietNatures,
      supplements: patient.supplements,
      restrictedFoodCategories: patient.restrictedFoodCategories,
    });

    this.nutritionStatesSharedCollection = this.nutritionStateService.addNutritionStateToCollectionIfMissing(
      this.nutritionStatesSharedCollection,
      patient.nutritionState
    );
    this.activityLevelsSharedCollection = this.activityLevelService.addActivityLevelToCollectionIfMissing(
      this.activityLevelsSharedCollection,
      patient.activityLevel
    );
    this.dietNaturesSharedCollection = this.dietNatureService.addDietNatureToCollectionIfMissing(
      this.dietNaturesSharedCollection,
      ...(patient.dietNatures ?? [])
    );
    this.supplementsSharedCollection = this.supplementsService.addSupplementsToCollectionIfMissing(
      this.supplementsSharedCollection,
      ...(patient.supplements ?? [])
    );
    this.foodCategoriesSharedCollection = this.foodCategoryService.addFoodCategoryToCollectionIfMissing(
      this.foodCategoriesSharedCollection,
      ...(patient.restrictedFoodCategories ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.nutritionStateService
      .query()
      .pipe(map((res: HttpResponse<INutritionState[]>) => res.body ?? []))
      .pipe(
        map((nutritionStates: INutritionState[]) =>
          this.nutritionStateService.addNutritionStateToCollectionIfMissing(nutritionStates, this.editForm.get('nutritionState')!.value)
        )
      )
      .subscribe((nutritionStates: INutritionState[]) => (this.nutritionStatesSharedCollection = nutritionStates));

    this.activityLevelService
      .query()
      .pipe(map((res: HttpResponse<IActivityLevel[]>) => res.body ?? []))
      .pipe(
        map((activityLevels: IActivityLevel[]) =>
          this.activityLevelService.addActivityLevelToCollectionIfMissing(activityLevels, this.editForm.get('activityLevel')!.value)
        )
      )
      .subscribe((activityLevels: IActivityLevel[]) => (this.activityLevelsSharedCollection = activityLevels));

    this.dietNatureService
      .query()
      .pipe(map((res: HttpResponse<IDietNature[]>) => res.body ?? []))
      .pipe(
        map((dietNatures: IDietNature[]) =>
          this.dietNatureService.addDietNatureToCollectionIfMissing(dietNatures, ...(this.editForm.get('dietNatures')!.value ?? []))
        )
      )
      .subscribe((dietNatures: IDietNature[]) => (this.dietNaturesSharedCollection = dietNatures));

    this.supplementsService
      .query()
      .pipe(map((res: HttpResponse<ISupplements[]>) => res.body ?? []))
      .pipe(
        map((supplements: ISupplements[]) =>
          this.supplementsService.addSupplementsToCollectionIfMissing(supplements, ...(this.editForm.get('supplements')!.value ?? []))
        )
      )
      .subscribe((supplements: ISupplements[]) => (this.supplementsSharedCollection = supplements));

    this.foodCategoryService
      .query()
      .pipe(map((res: HttpResponse<IFoodCategory[]>) => res.body ?? []))
      .pipe(
        map((foodCategories: IFoodCategory[]) =>
          this.foodCategoryService.addFoodCategoryToCollectionIfMissing(
            foodCategories,
            ...(this.editForm.get('restrictedFoodCategories')!.value ?? [])
          )
        )
      )
      .subscribe((foodCategories: IFoodCategory[]) => (this.foodCategoriesSharedCollection = foodCategories));
  }

  protected createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      hospital: this.editForm.get(['hospital'])!.value,
      admissionDate: this.editForm.get(['admissionDate'])!.value,
      reasonOfAdmission: this.editForm.get(['reasonOfAdmission'])!.value,
      wordNo: this.editForm.get(['wordNo'])!.value,
      bedNo: this.editForm.get(['bedNo'])!.value,
      healthCondition: this.editForm.get(['healthCondition'])!.value,
      mentalStatus: this.editForm.get(['mentalStatus'])!.value,
      age: this.editForm.get(['age'])!.value,
      sex: this.editForm.get(['sex'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      weightType: this.editForm.get(['weightType'])!.value,
      height: this.editForm.get(['height'])!.value,
      heightMeasureType: this.editForm.get(['heightMeasureType'])!.value,
      ibw: this.editForm.get(['ibw'])!.value,
      bmi: this.editForm.get(['bmi'])!.value,
      recentWeightGainLoss: this.editForm.get(['recentWeightGainLoss'])!.value,
      gainLossMeasure: this.editForm.get(['gainLossMeasure'])!.value,
      gainLossTimeFrame: this.editForm.get(['gainLossTimeFrame'])!.value,
      gainLossType: this.editForm.get(['gainLossType'])!.value,
      supplementTaken: this.editForm.get(['supplementTaken'])!.value,
      appetite: this.editForm.get(['appetite'])!.value,
      physicalActivity: this.editForm.get(['physicalActivity'])!.value,
      monthlyFamilyIncome: this.editForm.get(['monthlyFamilyIncome'])!.value,
      religion: this.editForm.get(['religion'])!.value,
      education: this.editForm.get(['education'])!.value,
      occupation: this.editForm.get(['occupation'])!.value,
      livingStatus: this.editForm.get(['livingStatus'])!.value,
      area: this.editForm.get(['area'])!.value,
      estimatedEnergyNeeds: this.editForm.get(['estimatedEnergyNeeds'])!.value,
      calculateEnergyNeeds: this.editForm.get(['calculateEnergyNeeds'])!.value,
      totalKCal: this.editForm.get(['totalKCal'])!.value,
      carbohydrate: this.editForm.get(['carbohydrate'])!.value,
      protein: this.editForm.get(['protein'])!.value,
      fat: this.editForm.get(['fat'])!.value,
      fluid: this.editForm.get(['fluid'])!.value,
      foodRestriction: this.editForm.get(['foodRestriction'])!.value,
      nutritionState: this.editForm.get(['nutritionState'])!.value,
      activityLevel: this.editForm.get(['activityLevel'])!.value,
      dietNatures: this.editForm.get(['dietNatures'])!.value,
      supplements: this.editForm.get(['supplements'])!.value,
      restrictedFoodCategories: this.editForm.get(['restrictedFoodCategories'])!.value,
    };
  }
}
