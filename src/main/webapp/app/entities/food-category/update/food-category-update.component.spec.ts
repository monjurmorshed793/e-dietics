jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodCategoryService } from '../service/food-category.service';
import { IFoodCategory, FoodCategory } from '../food-category.model';

import { FoodCategoryUpdateComponent } from './food-category-update.component';

describe('FoodCategory Management Update Component', () => {
  let comp: FoodCategoryUpdateComponent;
  let fixture: ComponentFixture<FoodCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let foodCategoryService: FoodCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FoodCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FoodCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FoodCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    foodCategoryService = TestBed.inject(FoodCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const foodCategory: IFoodCategory = { id: 'CBA' };

      activatedRoute.data = of({ foodCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(foodCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodCategory>>();
      const foodCategory = { id: 'ABC' };
      jest.spyOn(foodCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(foodCategoryService.update).toHaveBeenCalledWith(foodCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodCategory>>();
      const foodCategory = new FoodCategory();
      jest.spyOn(foodCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodCategory }));
      saveSubject.complete();

      // THEN
      expect(foodCategoryService.create).toHaveBeenCalledWith(foodCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodCategory>>();
      const foodCategory = { id: 'ABC' };
      jest.spyOn(foodCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(foodCategoryService.update).toHaveBeenCalledWith(foodCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
