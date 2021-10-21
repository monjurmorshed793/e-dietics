jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BiochemicalTestService } from '../service/biochemical-test.service';
import { IBiochemicalTest, BiochemicalTest } from '../biochemical-test.model';
import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';
import { MeasurementTypeService } from 'app/entities/measurement-type/service/measurement-type.service';

import { BiochemicalTestUpdateComponent } from './biochemical-test-update.component';

describe('BiochemicalTest Management Update Component', () => {
  let comp: BiochemicalTestUpdateComponent;
  let fixture: ComponentFixture<BiochemicalTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let biochemicalTestService: BiochemicalTestService;
  let measurementTypeService: MeasurementTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BiochemicalTestUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BiochemicalTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BiochemicalTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    biochemicalTestService = TestBed.inject(BiochemicalTestService);
    measurementTypeService = TestBed.inject(MeasurementTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MeasurementType query and add missing value', () => {
      const biochemicalTest: IBiochemicalTest = { id: 'CBA' };
      const defaultMeasurementType: IMeasurementType = { id: '0b428e0f-5aff-43cb-b97b-d25adc3eda59' };
      biochemicalTest.defaultMeasurementType = defaultMeasurementType;

      const measurementTypeCollection: IMeasurementType[] = [{ id: '7729212b-33e4-4564-8280-f267b4637623' }];
      jest.spyOn(measurementTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: measurementTypeCollection })));
      const additionalMeasurementTypes = [defaultMeasurementType];
      const expectedCollection: IMeasurementType[] = [...additionalMeasurementTypes, ...measurementTypeCollection];
      jest.spyOn(measurementTypeService, 'addMeasurementTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ biochemicalTest });
      comp.ngOnInit();

      expect(measurementTypeService.query).toHaveBeenCalled();
      expect(measurementTypeService.addMeasurementTypeToCollectionIfMissing).toHaveBeenCalledWith(
        measurementTypeCollection,
        ...additionalMeasurementTypes
      );
      expect(comp.measurementTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const biochemicalTest: IBiochemicalTest = { id: 'CBA' };
      const defaultMeasurementType: IMeasurementType = { id: 'ae6a31c4-c3a4-4389-80aa-d26204b0e696' };
      biochemicalTest.defaultMeasurementType = defaultMeasurementType;

      activatedRoute.data = of({ biochemicalTest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(biochemicalTest));
      expect(comp.measurementTypesSharedCollection).toContain(defaultMeasurementType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BiochemicalTest>>();
      const biochemicalTest = { id: 'ABC' };
      jest.spyOn(biochemicalTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ biochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: biochemicalTest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(biochemicalTestService.update).toHaveBeenCalledWith(biochemicalTest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BiochemicalTest>>();
      const biochemicalTest = new BiochemicalTest();
      jest.spyOn(biochemicalTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ biochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: biochemicalTest }));
      saveSubject.complete();

      // THEN
      expect(biochemicalTestService.create).toHaveBeenCalledWith(biochemicalTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BiochemicalTest>>();
      const biochemicalTest = { id: 'ABC' };
      jest.spyOn(biochemicalTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ biochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(biochemicalTestService.update).toHaveBeenCalledWith(biochemicalTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMeasurementTypeById', () => {
      it('Should return tracked MeasurementType primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackMeasurementTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
