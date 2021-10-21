jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PatientService } from '../service/patient.service';
import { IPatient, Patient } from '../patient.model';
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

import { PatientUpdateComponent } from './patient-update.component';

describe('Patient Management Update Component', () => {
  let comp: PatientUpdateComponent;
  let fixture: ComponentFixture<PatientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientService: PatientService;
  let nutritionStateService: NutritionStateService;
  let activityLevelService: ActivityLevelService;
  let dietNatureService: DietNatureService;
  let supplementsService: SupplementsService;
  let foodCategoryService: FoodCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PatientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientService = TestBed.inject(PatientService);
    nutritionStateService = TestBed.inject(NutritionStateService);
    activityLevelService = TestBed.inject(ActivityLevelService);
    dietNatureService = TestBed.inject(DietNatureService);
    supplementsService = TestBed.inject(SupplementsService);
    foodCategoryService = TestBed.inject(FoodCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call NutritionState query and add missing value', () => {
      const patient: IPatient = { id: 'CBA' };
      const nutritionState: INutritionState = { id: '17d4175f-af8f-4a55-aecf-b35da6cea4e5' };
      patient.nutritionState = nutritionState;

      const nutritionStateCollection: INutritionState[] = [{ id: 'd01934ed-8b68-4fc1-85cb-e01e909851ce' }];
      jest.spyOn(nutritionStateService, 'query').mockReturnValue(of(new HttpResponse({ body: nutritionStateCollection })));
      const additionalNutritionStates = [nutritionState];
      const expectedCollection: INutritionState[] = [...additionalNutritionStates, ...nutritionStateCollection];
      jest.spyOn(nutritionStateService, 'addNutritionStateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(nutritionStateService.query).toHaveBeenCalled();
      expect(nutritionStateService.addNutritionStateToCollectionIfMissing).toHaveBeenCalledWith(
        nutritionStateCollection,
        ...additionalNutritionStates
      );
      expect(comp.nutritionStatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ActivityLevel query and add missing value', () => {
      const patient: IPatient = { id: 'CBA' };
      const activityLevel: IActivityLevel = { id: '532763bd-b3c9-4cd0-b444-431025666840' };
      patient.activityLevel = activityLevel;

      const activityLevelCollection: IActivityLevel[] = [{ id: '0201981f-4129-4be0-928a-12d20d68e486' }];
      jest.spyOn(activityLevelService, 'query').mockReturnValue(of(new HttpResponse({ body: activityLevelCollection })));
      const additionalActivityLevels = [activityLevel];
      const expectedCollection: IActivityLevel[] = [...additionalActivityLevels, ...activityLevelCollection];
      jest.spyOn(activityLevelService, 'addActivityLevelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(activityLevelService.query).toHaveBeenCalled();
      expect(activityLevelService.addActivityLevelToCollectionIfMissing).toHaveBeenCalledWith(
        activityLevelCollection,
        ...additionalActivityLevels
      );
      expect(comp.activityLevelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DietNature query and add missing value', () => {
      const patient: IPatient = { id: 'CBA' };
      const dietNatures: IDietNature[] = [{ id: 'e7f6de64-712f-4674-8784-439361aa1550' }];
      patient.dietNatures = dietNatures;

      const dietNatureCollection: IDietNature[] = [{ id: '37c5cfb8-01f2-46d5-bb4c-855211594cda' }];
      jest.spyOn(dietNatureService, 'query').mockReturnValue(of(new HttpResponse({ body: dietNatureCollection })));
      const additionalDietNatures = [...dietNatures];
      const expectedCollection: IDietNature[] = [...additionalDietNatures, ...dietNatureCollection];
      jest.spyOn(dietNatureService, 'addDietNatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(dietNatureService.query).toHaveBeenCalled();
      expect(dietNatureService.addDietNatureToCollectionIfMissing).toHaveBeenCalledWith(dietNatureCollection, ...additionalDietNatures);
      expect(comp.dietNaturesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Supplements query and add missing value', () => {
      const patient: IPatient = { id: 'CBA' };
      const supplements: ISupplements[] = [{ id: 'fd6f0bd0-bc7b-462b-b19e-0642aa71fa0e' }];
      patient.supplements = supplements;

      const supplementsCollection: ISupplements[] = [{ id: 'e663dd7b-7a42-40c8-8aef-1e14b5f813ff' }];
      jest.spyOn(supplementsService, 'query').mockReturnValue(of(new HttpResponse({ body: supplementsCollection })));
      const additionalSupplements = [...supplements];
      const expectedCollection: ISupplements[] = [...additionalSupplements, ...supplementsCollection];
      jest.spyOn(supplementsService, 'addSupplementsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(supplementsService.query).toHaveBeenCalled();
      expect(supplementsService.addSupplementsToCollectionIfMissing).toHaveBeenCalledWith(supplementsCollection, ...additionalSupplements);
      expect(comp.supplementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FoodCategory query and add missing value', () => {
      const patient: IPatient = { id: 'CBA' };
      const restrictedFoodCategories: IFoodCategory[] = [{ id: '7de74645-fef7-4c6f-90ac-1043707654a4' }];
      patient.restrictedFoodCategories = restrictedFoodCategories;

      const foodCategoryCollection: IFoodCategory[] = [{ id: 'c99b5462-2871-4295-8394-fb6f44d3757b' }];
      jest.spyOn(foodCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: foodCategoryCollection })));
      const additionalFoodCategories = [...restrictedFoodCategories];
      const expectedCollection: IFoodCategory[] = [...additionalFoodCategories, ...foodCategoryCollection];
      jest.spyOn(foodCategoryService, 'addFoodCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(foodCategoryService.query).toHaveBeenCalled();
      expect(foodCategoryService.addFoodCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        foodCategoryCollection,
        ...additionalFoodCategories
      );
      expect(comp.foodCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patient: IPatient = { id: 'CBA' };
      const nutritionState: INutritionState = { id: '86be634f-4bcd-4e02-8cf4-f100e6dca7d2' };
      patient.nutritionState = nutritionState;
      const activityLevel: IActivityLevel = { id: '5ea13151-103b-48d4-81f3-aa58479eae2f' };
      patient.activityLevel = activityLevel;
      const dietNatures: IDietNature = { id: '2f25d588-2401-4e1c-a356-a5ae855b52ed' };
      patient.dietNatures = [dietNatures];
      const supplements: ISupplements = { id: '69a891a7-ce34-4662-99d1-f06f1dea1d0c' };
      patient.supplements = [supplements];
      const restrictedFoodCategories: IFoodCategory = { id: 'ecf1c874-30a4-4353-adfa-1b7ac5699b44' };
      patient.restrictedFoodCategories = [restrictedFoodCategories];

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patient));
      expect(comp.nutritionStatesSharedCollection).toContain(nutritionState);
      expect(comp.activityLevelsSharedCollection).toContain(activityLevel);
      expect(comp.dietNaturesSharedCollection).toContain(dietNatures);
      expect(comp.supplementsSharedCollection).toContain(supplements);
      expect(comp.foodCategoriesSharedCollection).toContain(restrictedFoodCategories);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patient>>();
      const patient = { id: 'ABC' };
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientService.update).toHaveBeenCalledWith(patient);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patient>>();
      const patient = new Patient();
      jest.spyOn(patientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(patientService.create).toHaveBeenCalledWith(patient);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patient>>();
      const patient = { id: 'ABC' };
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientService.update).toHaveBeenCalledWith(patient);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackNutritionStateById', () => {
      it('Should return tracked NutritionState primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackNutritionStateById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackActivityLevelById', () => {
      it('Should return tracked ActivityLevel primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackActivityLevelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDietNatureById', () => {
      it('Should return tracked DietNature primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackDietNatureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSupplementsById', () => {
      it('Should return tracked Supplements primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackSupplementsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFoodCategoryById', () => {
      it('Should return tracked FoodCategory primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackFoodCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDietNature', () => {
      it('Should return option if no DietNature is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedDietNature(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected DietNature for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedDietNature(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this DietNature is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedDietNature(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedSupplements', () => {
      it('Should return option if no Supplements is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedSupplements(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Supplements for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedSupplements(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Supplements is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedSupplements(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedFoodCategory', () => {
      it('Should return option if no FoodCategory is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedFoodCategory(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected FoodCategory for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedFoodCategory(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this FoodCategory is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedFoodCategory(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
