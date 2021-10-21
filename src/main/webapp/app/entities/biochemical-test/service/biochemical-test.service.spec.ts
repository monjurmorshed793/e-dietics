import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBiochemicalTest, BiochemicalTest } from '../biochemical-test.model';

import { BiochemicalTestService } from './biochemical-test.service';

describe('BiochemicalTest Service', () => {
  let service: BiochemicalTestService;
  let httpMock: HttpTestingController;
  let elemDefault: IBiochemicalTest;
  let expectedResult: IBiochemicalTest | IBiochemicalTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BiochemicalTestService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      testName: 'AAAAAAA',
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

    it('should create a BiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BiochemicalTest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          testName: 'BBBBBB',
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

    it('should partial update a BiochemicalTest', () => {
      const patchObject = Object.assign(
        {
          testName: 'BBBBBB',
        },
        new BiochemicalTest()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BiochemicalTest', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          testName: 'BBBBBB',
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

    it('should delete a BiochemicalTest', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBiochemicalTestToCollectionIfMissing', () => {
      it('should add a BiochemicalTest to an empty array', () => {
        const biochemicalTest: IBiochemicalTest = { id: 'ABC' };
        expectedResult = service.addBiochemicalTestToCollectionIfMissing([], biochemicalTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(biochemicalTest);
      });

      it('should not add a BiochemicalTest to an array that contains it', () => {
        const biochemicalTest: IBiochemicalTest = { id: 'ABC' };
        const biochemicalTestCollection: IBiochemicalTest[] = [
          {
            ...biochemicalTest,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addBiochemicalTestToCollectionIfMissing(biochemicalTestCollection, biochemicalTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BiochemicalTest to an array that doesn't contain it", () => {
        const biochemicalTest: IBiochemicalTest = { id: 'ABC' };
        const biochemicalTestCollection: IBiochemicalTest[] = [{ id: 'CBA' }];
        expectedResult = service.addBiochemicalTestToCollectionIfMissing(biochemicalTestCollection, biochemicalTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(biochemicalTest);
      });

      it('should add only unique BiochemicalTest to an array', () => {
        const biochemicalTestArray: IBiochemicalTest[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '237eb561-dbfa-4861-89f4-33bd53937928' }];
        const biochemicalTestCollection: IBiochemicalTest[] = [{ id: 'ABC' }];
        expectedResult = service.addBiochemicalTestToCollectionIfMissing(biochemicalTestCollection, ...biochemicalTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const biochemicalTest: IBiochemicalTest = { id: 'ABC' };
        const biochemicalTest2: IBiochemicalTest = { id: 'CBA' };
        expectedResult = service.addBiochemicalTestToCollectionIfMissing([], biochemicalTest, biochemicalTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(biochemicalTest);
        expect(expectedResult).toContain(biochemicalTest2);
      });

      it('should accept null and undefined values', () => {
        const biochemicalTest: IBiochemicalTest = { id: 'ABC' };
        expectedResult = service.addBiochemicalTestToCollectionIfMissing([], null, biochemicalTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(biochemicalTest);
      });

      it('should return initial array if no BiochemicalTest is added', () => {
        const biochemicalTestCollection: IBiochemicalTest[] = [{ id: 'ABC' }];
        expectedResult = service.addBiochemicalTestToCollectionIfMissing(biochemicalTestCollection, undefined, null);
        expect(expectedResult).toEqual(biochemicalTestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
