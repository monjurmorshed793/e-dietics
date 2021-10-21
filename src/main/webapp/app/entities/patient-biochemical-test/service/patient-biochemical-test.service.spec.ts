import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPatientBiochemicalTest, PatientBiochemicalTest } from '../patient-biochemical-test.model';

import { PatientBiochemicalTestService } from './patient-biochemical-test.service';

describe('PatientBiochemicalTest Service', () => {
  let service: PatientBiochemicalTestService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatientBiochemicalTest;
  let expectedResult: IPatientBiochemicalTest | IPatientBiochemicalTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatientBiochemicalTestService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      other: 'AAAAAAA',
      value: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PatientBiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PatientBiochemicalTest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PatientBiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          other: 'BBBBBB',
          value: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PatientBiochemicalTest', () => {
      const patchObject = Object.assign({}, new PatientBiochemicalTest());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PatientBiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          other: 'BBBBBB',
          value: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PatientBiochemicalTest', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatientBiochemicalTestToCollectionIfMissing', () => {
      it('should add a PatientBiochemicalTest to an empty array', () => {
        const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'ABC' };
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing([], patientBiochemicalTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientBiochemicalTest);
      });

      it('should not add a PatientBiochemicalTest to an array that contains it', () => {
        const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'ABC' };
        const patientBiochemicalTestCollection: IPatientBiochemicalTest[] = [
          {
            ...patientBiochemicalTest,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing(patientBiochemicalTestCollection, patientBiochemicalTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PatientBiochemicalTest to an array that doesn't contain it", () => {
        const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'ABC' };
        const patientBiochemicalTestCollection: IPatientBiochemicalTest[] = [{ id: 'CBA' }];
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing(patientBiochemicalTestCollection, patientBiochemicalTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientBiochemicalTest);
      });

      it('should add only unique PatientBiochemicalTest to an array', () => {
        const patientBiochemicalTestArray: IPatientBiochemicalTest[] = [
          { id: 'ABC' },
          { id: 'CBA' },
          { id: '33eff2e3-d3af-4278-823b-4d4c164b4c7c' },
        ];
        const patientBiochemicalTestCollection: IPatientBiochemicalTest[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing(
          patientBiochemicalTestCollection,
          ...patientBiochemicalTestArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'ABC' };
        const patientBiochemicalTest2: IPatientBiochemicalTest = { id: 'CBA' };
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing([], patientBiochemicalTest, patientBiochemicalTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientBiochemicalTest);
        expect(expectedResult).toContain(patientBiochemicalTest2);
      });

      it('should accept null and undefined values', () => {
        const patientBiochemicalTest: IPatientBiochemicalTest = { id: 'ABC' };
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing([], null, patientBiochemicalTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientBiochemicalTest);
      });

      it('should return initial array if no PatientBiochemicalTest is added', () => {
        const patientBiochemicalTestCollection: IPatientBiochemicalTest[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientBiochemicalTestToCollectionIfMissing(patientBiochemicalTestCollection, undefined, null);
        expect(expectedResult).toEqual(patientBiochemicalTestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
