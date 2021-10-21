import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeasurementType, MeasurementType } from '../measurement-type.model';

import { MeasurementTypeService } from './measurement-type.service';

describe('MeasurementType Service', () => {
  let service: MeasurementTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IMeasurementType;
  let expectedResult: IMeasurementType | IMeasurementType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeasurementTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      label: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a MeasurementType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MeasurementType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MeasurementType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          label: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MeasurementType', () => {
      const patchObject = Object.assign({}, new MeasurementType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MeasurementType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          label: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a MeasurementType', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMeasurementTypeToCollectionIfMissing', () => {
      it('should add a MeasurementType to an empty array', () => {
        const measurementType: IMeasurementType = { id: 'ABC' };
        expectedResult = service.addMeasurementTypeToCollectionIfMissing([], measurementType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measurementType);
      });

      it('should not add a MeasurementType to an array that contains it', () => {
        const measurementType: IMeasurementType = { id: 'ABC' };
        const measurementTypeCollection: IMeasurementType[] = [
          {
            ...measurementType,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addMeasurementTypeToCollectionIfMissing(measurementTypeCollection, measurementType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MeasurementType to an array that doesn't contain it", () => {
        const measurementType: IMeasurementType = { id: 'ABC' };
        const measurementTypeCollection: IMeasurementType[] = [{ id: 'CBA' }];
        expectedResult = service.addMeasurementTypeToCollectionIfMissing(measurementTypeCollection, measurementType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measurementType);
      });

      it('should add only unique MeasurementType to an array', () => {
        const measurementTypeArray: IMeasurementType[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '31424f27-2057-4d80-9d33-2b18de9ebc33' }];
        const measurementTypeCollection: IMeasurementType[] = [{ id: 'ABC' }];
        expectedResult = service.addMeasurementTypeToCollectionIfMissing(measurementTypeCollection, ...measurementTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const measurementType: IMeasurementType = { id: 'ABC' };
        const measurementType2: IMeasurementType = { id: 'CBA' };
        expectedResult = service.addMeasurementTypeToCollectionIfMissing([], measurementType, measurementType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measurementType);
        expect(expectedResult).toContain(measurementType2);
      });

      it('should accept null and undefined values', () => {
        const measurementType: IMeasurementType = { id: 'ABC' };
        expectedResult = service.addMeasurementTypeToCollectionIfMissing([], null, measurementType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measurementType);
      });

      it('should return initial array if no MeasurementType is added', () => {
        const measurementTypeCollection: IMeasurementType[] = [{ id: 'ABC' }];
        expectedResult = service.addMeasurementTypeToCollectionIfMissing(measurementTypeCollection, undefined, null);
        expect(expectedResult).toEqual(measurementTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
