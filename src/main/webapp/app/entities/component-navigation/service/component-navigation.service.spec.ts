import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IComponentNavigation, ComponentNavigation } from '../component-navigation.model';

import { ComponentNavigationService } from './component-navigation.service';

describe('ComponentNavigation Service', () => {
  let service: ComponentNavigationService;
  let httpMock: HttpTestingController;
  let elemDefault: IComponentNavigation;
  let expectedResult: IComponentNavigation | IComponentNavigation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComponentNavigationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      location: 'AAAAAAA',
      roles: 'AAAAAAA',
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

    it('should create a ComponentNavigation', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ComponentNavigation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ComponentNavigation', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          location: 'BBBBBB',
          roles: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ComponentNavigation', () => {
      const patchObject = Object.assign({}, new ComponentNavigation());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ComponentNavigation', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          location: 'BBBBBB',
          roles: 'BBBBBB',
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

    it('should delete a ComponentNavigation', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComponentNavigationToCollectionIfMissing', () => {
      it('should add a ComponentNavigation to an empty array', () => {
        const componentNavigation: IComponentNavigation = { id: 'ABC' };
        expectedResult = service.addComponentNavigationToCollectionIfMissing([], componentNavigation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(componentNavigation);
      });

      it('should not add a ComponentNavigation to an array that contains it', () => {
        const componentNavigation: IComponentNavigation = { id: 'ABC' };
        const componentNavigationCollection: IComponentNavigation[] = [
          {
            ...componentNavigation,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addComponentNavigationToCollectionIfMissing(componentNavigationCollection, componentNavigation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ComponentNavigation to an array that doesn't contain it", () => {
        const componentNavigation: IComponentNavigation = { id: 'ABC' };
        const componentNavigationCollection: IComponentNavigation[] = [{ id: 'CBA' }];
        expectedResult = service.addComponentNavigationToCollectionIfMissing(componentNavigationCollection, componentNavigation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(componentNavigation);
      });

      it('should add only unique ComponentNavigation to an array', () => {
        const componentNavigationArray: IComponentNavigation[] = [
          { id: 'ABC' },
          { id: 'CBA' },
          { id: '3a462474-d8bf-4264-a41c-ff4f5b384129' },
        ];
        const componentNavigationCollection: IComponentNavigation[] = [{ id: 'ABC' }];
        expectedResult = service.addComponentNavigationToCollectionIfMissing(componentNavigationCollection, ...componentNavigationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const componentNavigation: IComponentNavigation = { id: 'ABC' };
        const componentNavigation2: IComponentNavigation = { id: 'CBA' };
        expectedResult = service.addComponentNavigationToCollectionIfMissing([], componentNavigation, componentNavigation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(componentNavigation);
        expect(expectedResult).toContain(componentNavigation2);
      });

      it('should accept null and undefined values', () => {
        const componentNavigation: IComponentNavigation = { id: 'ABC' };
        expectedResult = service.addComponentNavigationToCollectionIfMissing([], null, componentNavigation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(componentNavigation);
      });

      it('should return initial array if no ComponentNavigation is added', () => {
        const componentNavigationCollection: IComponentNavigation[] = [{ id: 'ABC' }];
        expectedResult = service.addComponentNavigationToCollectionIfMissing(componentNavigationCollection, undefined, null);
        expect(expectedResult).toEqual(componentNavigationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
