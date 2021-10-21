import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActivityLevel, ActivityLevel } from '../activity-level.model';

import { ActivityLevelService } from './activity-level.service';

describe('ActivityLevel Service', () => {
  let service: ActivityLevelService;
  let httpMock: HttpTestingController;
  let elemDefault: IActivityLevel;
  let expectedResult: IActivityLevel | IActivityLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActivityLevelService);
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

    it('should create a ActivityLevel', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ActivityLevel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActivityLevel', () => {
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

    it('should partial update a ActivityLevel', () => {
      const patchObject = Object.assign(
        {
          label: 'BBBBBB',
        },
        new ActivityLevel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActivityLevel', () => {
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

    it('should delete a ActivityLevel', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActivityLevelToCollectionIfMissing', () => {
      it('should add a ActivityLevel to an empty array', () => {
        const activityLevel: IActivityLevel = { id: 'ABC' };
        expectedResult = service.addActivityLevelToCollectionIfMissing([], activityLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityLevel);
      });

      it('should not add a ActivityLevel to an array that contains it', () => {
        const activityLevel: IActivityLevel = { id: 'ABC' };
        const activityLevelCollection: IActivityLevel[] = [
          {
            ...activityLevel,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addActivityLevelToCollectionIfMissing(activityLevelCollection, activityLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActivityLevel to an array that doesn't contain it", () => {
        const activityLevel: IActivityLevel = { id: 'ABC' };
        const activityLevelCollection: IActivityLevel[] = [{ id: 'CBA' }];
        expectedResult = service.addActivityLevelToCollectionIfMissing(activityLevelCollection, activityLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityLevel);
      });

      it('should add only unique ActivityLevel to an array', () => {
        const activityLevelArray: IActivityLevel[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'e3c93d92-5f89-4d38-8338-e5f42deb253b' }];
        const activityLevelCollection: IActivityLevel[] = [{ id: 'ABC' }];
        expectedResult = service.addActivityLevelToCollectionIfMissing(activityLevelCollection, ...activityLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activityLevel: IActivityLevel = { id: 'ABC' };
        const activityLevel2: IActivityLevel = { id: 'CBA' };
        expectedResult = service.addActivityLevelToCollectionIfMissing([], activityLevel, activityLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityLevel);
        expect(expectedResult).toContain(activityLevel2);
      });

      it('should accept null and undefined values', () => {
        const activityLevel: IActivityLevel = { id: 'ABC' };
        expectedResult = service.addActivityLevelToCollectionIfMissing([], null, activityLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityLevel);
      });

      it('should return initial array if no ActivityLevel is added', () => {
        const activityLevelCollection: IActivityLevel[] = [{ id: 'ABC' }];
        expectedResult = service.addActivityLevelToCollectionIfMissing(activityLevelCollection, undefined, null);
        expect(expectedResult).toEqual(activityLevelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
