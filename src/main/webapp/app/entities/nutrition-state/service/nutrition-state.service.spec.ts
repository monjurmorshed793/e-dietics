import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INutritionState, NutritionState } from '../nutrition-state.model';

import { NutritionStateService } from './nutrition-state.service';

describe('NutritionState Service', () => {
  let service: NutritionStateService;
  let httpMock: HttpTestingController;
  let elemDefault: INutritionState;
  let expectedResult: INutritionState | INutritionState[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NutritionStateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      order: 0,
      label: 'AAAAAAA',
      note: 'AAAAAAA',
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

    it('should create a NutritionState', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NutritionState()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NutritionState', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          order: 1,
          label: 'BBBBBB',
          note: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NutritionState', () => {
      const patchObject = Object.assign(
        {
          order: 1,
          label: 'BBBBBB',
          note: 'BBBBBB',
        },
        new NutritionState()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NutritionState', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          order: 1,
          label: 'BBBBBB',
          note: 'BBBBBB',
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

    it('should delete a NutritionState', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNutritionStateToCollectionIfMissing', () => {
      it('should add a NutritionState to an empty array', () => {
        const nutritionState: INutritionState = { id: 'ABC' };
        expectedResult = service.addNutritionStateToCollectionIfMissing([], nutritionState);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nutritionState);
      });

      it('should not add a NutritionState to an array that contains it', () => {
        const nutritionState: INutritionState = { id: 'ABC' };
        const nutritionStateCollection: INutritionState[] = [
          {
            ...nutritionState,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addNutritionStateToCollectionIfMissing(nutritionStateCollection, nutritionState);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NutritionState to an array that doesn't contain it", () => {
        const nutritionState: INutritionState = { id: 'ABC' };
        const nutritionStateCollection: INutritionState[] = [{ id: 'CBA' }];
        expectedResult = service.addNutritionStateToCollectionIfMissing(nutritionStateCollection, nutritionState);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nutritionState);
      });

      it('should add only unique NutritionState to an array', () => {
        const nutritionStateArray: INutritionState[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '51188947-1cd4-4a06-a305-14dd2db492a6' }];
        const nutritionStateCollection: INutritionState[] = [{ id: 'ABC' }];
        expectedResult = service.addNutritionStateToCollectionIfMissing(nutritionStateCollection, ...nutritionStateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nutritionState: INutritionState = { id: 'ABC' };
        const nutritionState2: INutritionState = { id: 'CBA' };
        expectedResult = service.addNutritionStateToCollectionIfMissing([], nutritionState, nutritionState2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nutritionState);
        expect(expectedResult).toContain(nutritionState2);
      });

      it('should accept null and undefined values', () => {
        const nutritionState: INutritionState = { id: 'ABC' };
        expectedResult = service.addNutritionStateToCollectionIfMissing([], null, nutritionState, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nutritionState);
      });

      it('should return initial array if no NutritionState is added', () => {
        const nutritionStateCollection: INutritionState[] = [{ id: 'ABC' }];
        expectedResult = service.addNutritionStateToCollectionIfMissing(nutritionStateCollection, undefined, null);
        expect(expectedResult).toEqual(nutritionStateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
