jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ComponentNavigationService } from '../service/component-navigation.service';
import { IComponentNavigation, ComponentNavigation } from '../component-navigation.model';

import { ComponentNavigationUpdateComponent } from './component-navigation-update.component';

describe('ComponentNavigation Management Update Component', () => {
  let comp: ComponentNavigationUpdateComponent;
  let fixture: ComponentFixture<ComponentNavigationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let componentNavigationService: ComponentNavigationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ComponentNavigationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ComponentNavigationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComponentNavigationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    componentNavigationService = TestBed.inject(ComponentNavigationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ComponentNavigation query and add missing value', () => {
      const componentNavigation: IComponentNavigation = { id: 'CBA' };
      const parent: IComponentNavigation = { id: '5659dc4f-d023-4a3c-921c-ea40c303e439' };
      componentNavigation.parent = parent;

      const componentNavigationCollection: IComponentNavigation[] = [{ id: '06ede5ab-7e5d-47e1-9730-112121f0cd93' }];
      jest.spyOn(componentNavigationService, 'query').mockReturnValue(of(new HttpResponse({ body: componentNavigationCollection })));
      const additionalComponentNavigations = [parent];
      const expectedCollection: IComponentNavigation[] = [...additionalComponentNavigations, ...componentNavigationCollection];
      jest.spyOn(componentNavigationService, 'addComponentNavigationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ componentNavigation });
      comp.ngOnInit();

      expect(componentNavigationService.query).toHaveBeenCalled();
      expect(componentNavigationService.addComponentNavigationToCollectionIfMissing).toHaveBeenCalledWith(
        componentNavigationCollection,
        ...additionalComponentNavigations
      );
      expect(comp.componentNavigationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const componentNavigation: IComponentNavigation = { id: 'CBA' };
      const parent: IComponentNavigation = { id: '04e57b64-459a-467c-938e-dd024ed70cd2' };
      componentNavigation.parent = parent;

      activatedRoute.data = of({ componentNavigation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(componentNavigation));
      expect(comp.componentNavigationsSharedCollection).toContain(parent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComponentNavigation>>();
      const componentNavigation = { id: 'ABC' };
      jest.spyOn(componentNavigationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ componentNavigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: componentNavigation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(componentNavigationService.update).toHaveBeenCalledWith(componentNavigation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComponentNavigation>>();
      const componentNavigation = new ComponentNavigation();
      jest.spyOn(componentNavigationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ componentNavigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: componentNavigation }));
      saveSubject.complete();

      // THEN
      expect(componentNavigationService.create).toHaveBeenCalledWith(componentNavigation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComponentNavigation>>();
      const componentNavigation = { id: 'ABC' };
      jest.spyOn(componentNavigationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ componentNavigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(componentNavigationService.update).toHaveBeenCalledWith(componentNavigation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackComponentNavigationById', () => {
      it('Should return tracked ComponentNavigation primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackComponentNavigationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
