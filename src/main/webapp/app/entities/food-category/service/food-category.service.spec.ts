import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFoodCategory, FoodCategory } from '../food-category.model';

import { FoodCategoryService } from './food-category.service';

describe('FoodCategory Service', () => {
  let service: FoodCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IFoodCategory;
  let expectedResult: IFoodCategory | IFoodCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FoodCategoryService);
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

    it('should create a FoodCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FoodCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FoodCategory', () => {
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

    it('should partial update a FoodCategory', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        new FoodCategory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FoodCategory', () => {
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

    it('should delete a FoodCategory', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFoodCategoryToCollectionIfMissing', () => {
      it('should add a FoodCategory to an empty array', () => {
        const foodCategory: IFoodCategory = { id: 'ABC' };
        expectedResult = service.addFoodCategoryToCollectionIfMissing([], foodCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodCategory);
      });

      it('should not add a FoodCategory to an array that contains it', () => {
        const foodCategory: IFoodCategory = { id: 'ABC' };
        const foodCategoryCollection: IFoodCategory[] = [
          {
            ...foodCategory,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addFoodCategoryToCollectionIfMissing(foodCategoryCollection, foodCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FoodCategory to an array that doesn't contain it", () => {
        const foodCategory: IFoodCategory = { id: 'ABC' };
        const foodCategoryCollection: IFoodCategory[] = [{ id: 'CBA' }];
        expectedResult = service.addFoodCategoryToCollectionIfMissing(foodCategoryCollection, foodCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodCategory);
      });

      it('should add only unique FoodCategory to an array', () => {
        const foodCategoryArray: IFoodCategory[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'fbcc5457-fa6a-41b1-96cb-5af9b47aefe6' }];
        const foodCategoryCollection: IFoodCategory[] = [{ id: 'ABC' }];
        expectedResult = service.addFoodCategoryToCollectionIfMissing(foodCategoryCollection, ...foodCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const foodCategory: IFoodCategory = { id: 'ABC' };
        const foodCategory2: IFoodCategory = { id: 'CBA' };
        expectedResult = service.addFoodCategoryToCollectionIfMissing([], foodCategory, foodCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodCategory);
        expect(expectedResult).toContain(foodCategory2);
      });

      it('should accept null and undefined values', () => {
        const foodCategory: IFoodCategory = { id: 'ABC' };
        expectedResult = service.addFoodCategoryToCollectionIfMissing([], null, foodCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodCategory);
      });

      it('should return initial array if no FoodCategory is added', () => {
        const foodCategoryCollection: IFoodCategory[] = [{ id: 'ABC' }];
        expectedResult = service.addFoodCategoryToCollectionIfMissing(foodCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(foodCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
