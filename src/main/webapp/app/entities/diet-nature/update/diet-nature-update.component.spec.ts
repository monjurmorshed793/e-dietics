jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DietNatureService } from '../service/diet-nature.service';
import { IDietNature, DietNature } from '../diet-nature.model';

import { DietNatureUpdateComponent } from './diet-nature-update.component';

describe('DietNature Management Update Component', () => {
  let comp: DietNatureUpdateComponent;
  let fixture: ComponentFixture<DietNatureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dietNatureService: DietNatureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DietNatureUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DietNatureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DietNatureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dietNatureService = TestBed.inject(DietNatureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dietNature: IDietNature = { id: 'CBA' };

      activatedRoute.data = of({ dietNature });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dietNature));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DietNature>>();
      const dietNature = { id: 'ABC' };
      jest.spyOn(dietNatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dietNature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dietNature }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dietNatureService.update).toHaveBeenCalledWith(dietNature);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DietNature>>();
      const dietNature = new DietNature();
      jest.spyOn(dietNatureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dietNature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dietNature }));
      saveSubject.complete();

      // THEN
      expect(dietNatureService.create).toHaveBeenCalledWith(dietNature);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DietNature>>();
      const dietNature = { id: 'ABC' };
      jest.spyOn(dietNatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dietNature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dietNatureService.update).toHaveBeenCalledWith(dietNature);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
