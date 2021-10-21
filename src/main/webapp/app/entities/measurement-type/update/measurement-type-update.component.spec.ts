jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MeasurementTypeService } from '../service/measurement-type.service';
import { IMeasurementType, MeasurementType } from '../measurement-type.model';

import { MeasurementTypeUpdateComponent } from './measurement-type-update.component';

describe('MeasurementType Management Update Component', () => {
  let comp: MeasurementTypeUpdateComponent;
  let fixture: ComponentFixture<MeasurementTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let measurementTypeService: MeasurementTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeasurementTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MeasurementTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeasurementTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    measurementTypeService = TestBed.inject(MeasurementTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const measurementType: IMeasurementType = { id: 'CBA' };

      activatedRoute.data = of({ measurementType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(measurementType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasurementType>>();
      const measurementType = { id: 'ABC' };
      jest.spyOn(measurementTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurementType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measurementType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(measurementTypeService.update).toHaveBeenCalledWith(measurementType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasurementType>>();
      const measurementType = new MeasurementType();
      jest.spyOn(measurementTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurementType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measurementType }));
      saveSubject.complete();

      // THEN
      expect(measurementTypeService.create).toHaveBeenCalledWith(measurementType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasurementType>>();
      const measurementType = { id: 'ABC' };
      jest.spyOn(measurementTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurementType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(measurementTypeService.update).toHaveBeenCalledWith(measurementType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
