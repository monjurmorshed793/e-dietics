import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { WeightType } from 'app/entities/enumerations/weight-type.model';
import { HeightMeasureType } from 'app/entities/enumerations/height-measure-type.model';
import { GainLossType } from 'app/entities/enumerations/gain-loss-type.model';
import { AppetiteType } from 'app/entities/enumerations/appetite-type.model';
import { PhysicalActivityType } from 'app/entities/enumerations/physical-activity-type.model';
import { ReligionType } from 'app/entities/enumerations/religion-type.model';
import { AreaType } from 'app/entities/enumerations/area-type.model';
import { IPatient, Patient } from '../patient.model';

import { PatientService } from './patient.service';

describe('Patient Service', () => {
  let service: PatientService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatient;
  let expectedResult: IPatient | IPatient[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatientService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      address: 'AAAAAAA',
      hospital: 'AAAAAAA',
      admissionDate: currentDate,
      reasonOfAdmission: 'AAAAAAA',
      wordNo: 'AAAAAAA',
      bedNo: 'AAAAAAA',
      healthCondition: 'AAAAAAA',
      mentalStatus: 'AAAAAAA',
      age: 0,
      sex: Gender.MALE,
      weight: 0,
      weightType: WeightType.UNDERWEIGHT,
      height: 0,
      heightMeasureType: HeightMeasureType.CM,
      ibw: 0,
      bmi: 0,
      recentWeightGainLoss: false,
      gainLossMeasure: 0,
      gainLossTimeFrame: 0,
      gainLossType: GainLossType.INTENTIONAL,
      supplementTaken: false,
      appetite: AppetiteType.EXCELLENT,
      physicalActivity: PhysicalActivityType.REGULAR,
      monthlyFamilyIncome: 0,
      religion: ReligionType.MUSLIM,
      education: 'AAAAAAA',
      occupation: 'AAAAAAA',
      livingStatus: 'AAAAAAA',
      area: AreaType.RURAL,
      estimatedEnergyNeeds: 'AAAAAAA',
      calculateEnergyNeeds: 'AAAAAAA',
      totalKCal: 0,
      carbohydrate: 0,
      protein: 0,
      fat: 0,
      fluid: 0,
      foodRestriction: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          admissionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Patient', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          admissionDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          admissionDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Patient()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Patient', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          address: 'BBBBBB',
          hospital: 'BBBBBB',
          admissionDate: currentDate.format(DATE_FORMAT),
          reasonOfAdmission: 'BBBBBB',
          wordNo: 'BBBBBB',
          bedNo: 'BBBBBB',
          healthCondition: 'BBBBBB',
          mentalStatus: 'BBBBBB',
          age: 1,
          sex: 'BBBBBB',
          weight: 1,
          weightType: 'BBBBBB',
          height: 1,
          heightMeasureType: 'BBBBBB',
          ibw: 1,
          bmi: 1,
          recentWeightGainLoss: true,
          gainLossMeasure: 1,
          gainLossTimeFrame: 1,
          gainLossType: 'BBBBBB',
          supplementTaken: true,
          appetite: 'BBBBBB',
          physicalActivity: 'BBBBBB',
          monthlyFamilyIncome: 1,
          religion: 'BBBBBB',
          education: 'BBBBBB',
          occupation: 'BBBBBB',
          livingStatus: 'BBBBBB',
          area: 'BBBBBB',
          estimatedEnergyNeeds: 'BBBBBB',
          calculateEnergyNeeds: 'BBBBBB',
          totalKCal: 1,
          carbohydrate: 1,
          protein: 1,
          fat: 1,
          fluid: 1,
          foodRestriction: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          admissionDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Patient', () => {
      const patchObject = Object.assign(
        {
          address: 'BBBBBB',
          hospital: 'BBBBBB',
          bedNo: 'BBBBBB',
          mentalStatus: 'BBBBBB',
          weightType: 'BBBBBB',
          height: 1,
          physicalActivity: 'BBBBBB',
          occupation: 'BBBBBB',
          area: 'BBBBBB',
          estimatedEnergyNeeds: 'BBBBBB',
          calculateEnergyNeeds: 'BBBBBB',
          totalKCal: 1,
          carbohydrate: 1,
          fat: 1,
        },
        new Patient()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          admissionDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Patient', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          address: 'BBBBBB',
          hospital: 'BBBBBB',
          admissionDate: currentDate.format(DATE_FORMAT),
          reasonOfAdmission: 'BBBBBB',
          wordNo: 'BBBBBB',
          bedNo: 'BBBBBB',
          healthCondition: 'BBBBBB',
          mentalStatus: 'BBBBBB',
          age: 1,
          sex: 'BBBBBB',
          weight: 1,
          weightType: 'BBBBBB',
          height: 1,
          heightMeasureType: 'BBBBBB',
          ibw: 1,
          bmi: 1,
          recentWeightGainLoss: true,
          gainLossMeasure: 1,
          gainLossTimeFrame: 1,
          gainLossType: 'BBBBBB',
          supplementTaken: true,
          appetite: 'BBBBBB',
          physicalActivity: 'BBBBBB',
          monthlyFamilyIncome: 1,
          religion: 'BBBBBB',
          education: 'BBBBBB',
          occupation: 'BBBBBB',
          livingStatus: 'BBBBBB',
          area: 'BBBBBB',
          estimatedEnergyNeeds: 'BBBBBB',
          calculateEnergyNeeds: 'BBBBBB',
          totalKCal: 1,
          carbohydrate: 1,
          protein: 1,
          fat: 1,
          fluid: 1,
          foodRestriction: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          admissionDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Patient', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatientToCollectionIfMissing', () => {
      it('should add a Patient to an empty array', () => {
        const patient: IPatient = { id: 'ABC' };
        expectedResult = service.addPatientToCollectionIfMissing([], patient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patient);
      });

      it('should not add a Patient to an array that contains it', () => {
        const patient: IPatient = { id: 'ABC' };
        const patientCollection: IPatient[] = [
          {
            ...patient,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addPatientToCollectionIfMissing(patientCollection, patient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Patient to an array that doesn't contain it", () => {
        const patient: IPatient = { id: 'ABC' };
        const patientCollection: IPatient[] = [{ id: 'CBA' }];
        expectedResult = service.addPatientToCollectionIfMissing(patientCollection, patient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patient);
      });

      it('should add only unique Patient to an array', () => {
        const patientArray: IPatient[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '637ad74d-3113-4650-b8bd-0f5759093e70' }];
        const patientCollection: IPatient[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientToCollectionIfMissing(patientCollection, ...patientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patient: IPatient = { id: 'ABC' };
        const patient2: IPatient = { id: 'CBA' };
        expectedResult = service.addPatientToCollectionIfMissing([], patient, patient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patient);
        expect(expectedResult).toContain(patient2);
      });

      it('should accept null and undefined values', () => {
        const patient: IPatient = { id: 'ABC' };
        expectedResult = service.addPatientToCollectionIfMissing([], null, patient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patient);
      });

      it('should return initial array if no Patient is added', () => {
        const patientCollection: IPatient[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientToCollectionIfMissing(patientCollection, undefined, null);
        expect(expectedResult).toEqual(patientCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
