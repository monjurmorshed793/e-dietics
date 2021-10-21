import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDietNature, DietNature } from '../diet-nature.model';

import { DietNatureService } from './diet-nature.service';

describe('DietNature Service', () => {
  let service: DietNatureService;
  let httpMock: HttpTestingController;
  let elemDefault: IDietNature;
  let expectedResult: IDietNature | IDietNature[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DietNatureService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
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

    it('should create a DietNature', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DietNature()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DietNature', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
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

    it('should partial update a DietNature', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new DietNature()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DietNature', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
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

    it('should delete a DietNature', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDietNatureToCollectionIfMissing', () => {
      it('should add a DietNature to an empty array', () => {
        const dietNature: IDietNature = { id: 'ABC' };
        expectedResult = service.addDietNatureToCollectionIfMissing([], dietNature);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dietNature);
      });

      it('should not add a DietNature to an array that contains it', () => {
        const dietNature: IDietNature = { id: 'ABC' };
        const dietNatureCollection: IDietNature[] = [
          {
            ...dietNature,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addDietNatureToCollectionIfMissing(dietNatureCollection, dietNature);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DietNature to an array that doesn't contain it", () => {
        const dietNature: IDietNature = { id: 'ABC' };
        const dietNatureCollection: IDietNature[] = [{ id: 'CBA' }];
        expectedResult = service.addDietNatureToCollectionIfMissing(dietNatureCollection, dietNature);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dietNature);
      });

      it('should add only unique DietNature to an array', () => {
        const dietNatureArray: IDietNature[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '8d0f3a34-ac4d-4ee5-8c10-ad183345d565' }];
        const dietNatureCollection: IDietNature[] = [{ id: 'ABC' }];
        expectedResult = service.addDietNatureToCollectionIfMissing(dietNatureCollection, ...dietNatureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dietNature: IDietNature = { id: 'ABC' };
        const dietNature2: IDietNature = { id: 'CBA' };
        expectedResult = service.addDietNatureToCollectionIfMissing([], dietNature, dietNature2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dietNature);
        expect(expectedResult).toContain(dietNature2);
      });

      it('should accept null and undefined values', () => {
        const dietNature: IDietNature = { id: 'ABC' };
        expectedResult = service.addDietNatureToCollectionIfMissing([], null, dietNature, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dietNature);
      });

      it('should return initial array if no DietNature is added', () => {
        const dietNatureCollection: IDietNature[] = [{ id: 'ABC' }];
        expectedResult = service.addDietNatureToCollectionIfMissing(dietNatureCollection, undefined, null);
        expect(expectedResult).toEqual(dietNatureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
