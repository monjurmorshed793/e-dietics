import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISupplements, Supplements } from '../supplements.model';

import { SupplementsService } from './supplements.service';

describe('Supplements Service', () => {
  let service: SupplementsService;
  let httpMock: HttpTestingController;
  let elemDefault: ISupplements;
  let expectedResult: ISupplements | ISupplements[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SupplementsService);
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

    it('should create a Supplements', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Supplements()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Supplements', () => {
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

    it('should partial update a Supplements', () => {
      const patchObject = Object.assign({}, new Supplements());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Supplements', () => {
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

    it('should delete a Supplements', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSupplementsToCollectionIfMissing', () => {
      it('should add a Supplements to an empty array', () => {
        const supplements: ISupplements = { id: 'ABC' };
        expectedResult = service.addSupplementsToCollectionIfMissing([], supplements);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplements);
      });

      it('should not add a Supplements to an array that contains it', () => {
        const supplements: ISupplements = { id: 'ABC' };
        const supplementsCollection: ISupplements[] = [
          {
            ...supplements,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addSupplementsToCollectionIfMissing(supplementsCollection, supplements);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Supplements to an array that doesn't contain it", () => {
        const supplements: ISupplements = { id: 'ABC' };
        const supplementsCollection: ISupplements[] = [{ id: 'CBA' }];
        expectedResult = service.addSupplementsToCollectionIfMissing(supplementsCollection, supplements);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplements);
      });

      it('should add only unique Supplements to an array', () => {
        const supplementsArray: ISupplements[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '0d5e320e-0c86-4711-b160-edc8face350e' }];
        const supplementsCollection: ISupplements[] = [{ id: 'ABC' }];
        expectedResult = service.addSupplementsToCollectionIfMissing(supplementsCollection, ...supplementsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const supplements: ISupplements = { id: 'ABC' };
        const supplements2: ISupplements = { id: 'CBA' };
        expectedResult = service.addSupplementsToCollectionIfMissing([], supplements, supplements2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplements);
        expect(expectedResult).toContain(supplements2);
      });

      it('should accept null and undefined values', () => {
        const supplements: ISupplements = { id: 'ABC' };
        expectedResult = service.addSupplementsToCollectionIfMissing([], null, supplements, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplements);
      });

      it('should return initial array if no Supplements is added', () => {
        const supplementsCollection: ISupplements[] = [{ id: 'ABC' }];
        expectedResult = service.addSupplementsToCollectionIfMissing(supplementsCollection, undefined, null);
        expect(expectedResult).toEqual(supplementsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
