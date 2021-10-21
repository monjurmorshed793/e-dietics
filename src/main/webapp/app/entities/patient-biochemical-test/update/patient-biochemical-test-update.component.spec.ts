jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';
import { IPatientBiochemicalTest, PatientBiochemicalTest } from '../patient-biochemical-test.model';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IBiochemicalTest } from 'app/entities/biochemical-test/biochemical-test.model';
import { BiochemicalTestService } from 'app/entities/biochemical-test/service/biochemical-test.service';
import { IMeasurementType } from 'app/entities/measurement-type/measurement-type.model';
import { MeasurementTypeService } from 'app/entities/measurement-type/service/measurement-type.service';

import { PatientBiochemicalTestUpdateComponent } from './patient-biochemical-test-update.component';

describe('PatientBiochemicalTest Management Update Component', () => {
  let comp: PatientBiochemicalTestUpdateComponent;
  let fixture: ComponentFixture<PatientBiochemicalTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientBiochemicalTestService: PatientBiochemicalTestService;
  let patientService: PatientService;
  let biochemicalTestService: BiochemicalTestService;
  let measurementTypeService: MeasurementTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientBiochemicalTestUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PatientBiochemicalTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientBiochemicalTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientBiochemicalTestService = TestBed.inject(PatientBiochemicalTestService);
    patientService = TestBed.inject(PatientService);
    biochemicalTestService = TestBed.inject(BiochemicalTestService);
    measurementTypeService = TestBed.inject(MeasurementTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patient query and add missing value', () => {
      const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'CBA' };
      const patient: IPatient = { id: '142cec78-f8a0-4302-a290-6fc2def2c2ab' };
      patientBiochemicalTest.patient = patient;

      const patientCollection: IPatient[] = [{ id: 'c4f7f252-d2dd-4f32-af33-50187a74f74e' }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(patientCollection, ...additionalPatients);
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BiochemicalTest query and add missing value', () => {
      const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'CBA' };
      const biochemicalTest: IBiochemicalTest = { id: '184e9d33-c203-4a62-84c5-46146a71d0ad' };
      patientBiochemicalTest.biochemicalTest = biochemicalTest;

      const biochemicalTestCollection: IBiochemicalTest[] = [{ id: '50a98ad3-ce23-4d05-bde2-a4164ae6073b' }];
      jest.spyOn(biochemicalTestService, 'query').mockReturnValue(of(new HttpResponse({ body: biochemicalTestCollection })));
      const additionalBiochemicalTests = [biochemicalTest];
      const expectedCollection: IBiochemicalTest[] = [...additionalBiochemicalTests, ...biochemicalTestCollection];
      jest.spyOn(biochemicalTestService, 'addBiochemicalTestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      expect(biochemicalTestService.query).toHaveBeenCalled();
      expect(biochemicalTestService.addBiochemicalTestToCollectionIfMissing).toHaveBeenCalledWith(
        biochemicalTestCollection,
        ...additionalBiochemicalTests
      );
      expect(comp.biochemicalTestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MeasurementType query and add missing value', () => {
      const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'CBA' };
      const measurementType: IMeasurementType = { id: '69e7855b-b391-4e70-9083-5e7b1df089b9' };
      patientBiochemicalTest.measurementType = measurementType;

      const measurementTypeCollection: IMeasurementType[] = [{ id: 'f23a6b5c-f98d-4fe6-b075-281377445d83' }];
      jest.spyOn(measurementTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: measurementTypeCollection })));
      const additionalMeasurementTypes = [measurementType];
      const expectedCollection: IMeasurementType[] = [...additionalMeasurementTypes, ...measurementTypeCollection];
      jest.spyOn(measurementTypeService, 'addMeasurementTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      expect(measurementTypeService.query).toHaveBeenCalled();
      expect(measurementTypeService.addMeasurementTypeToCollectionIfMissing).toHaveBeenCalledWith(
        measurementTypeCollection,
        ...additionalMeasurementTypes
      );
      expect(comp.measurementTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'CBA' };
      const patient: IPatient = { id: '7fdb2a0d-b92b-41ff-a76a-f397a1cd6cd0' };
      patientBiochemicalTest.patient = patient;
      const biochemicalTest: IBiochemicalTest = { id: 'e576c557-425c-4f31-a917-51711d0734a9' };
      patientBiochemicalTest.biochemicalTest = biochemicalTest;
      const measurementType: IMeasurementType = { id: 'fa008e54-912f-4331-9914-bda2419e57b2' };
      patientBiochemicalTest.measurementType = measurementType;

      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patientBiochemicalTest));
      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.biochemicalTestsSharedCollection).toContain(biochemicalTest);
      expect(comp.measurementTypesSharedCollection).toContain(measurementType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientBiochemicalTest>>();
      const patientBiochemicalTest = { id: 'ABC' };
      jest.spyOn(patientBiochemicalTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientBiochemicalTest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientBiochemicalTestService.update).toHaveBeenCalledWith(patientBiochemicalTest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientBiochemicalTest>>();
      const patientBiochemicalTest = new PatientBiochemicalTest();
      jest.spyOn(patientBiochemicalTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientBiochemicalTest }));
      saveSubject.complete();

      // THEN
      expect(patientBiochemicalTestService.create).toHaveBeenCalledWith(patientBiochemicalTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientBiochemicalTest>>();
      const patientBiochemicalTest = { id: 'ABC' };
      jest.spyOn(patientBiochemicalTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientBiochemicalTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientBiochemicalTestService.update).toHaveBeenCalledWith(patientBiochemicalTest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPatientById', () => {
      it('Should return tracked Patient primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackPatientById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBiochemicalTestById', () => {
      it('Should return tracked BiochemicalTest primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackBiochemicalTestById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMeasurementTypeById', () => {
      it('Should return tracked MeasurementType primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackMeasurementTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
