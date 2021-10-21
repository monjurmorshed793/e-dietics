jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SupplementsService } from '../service/supplements.service';
import { ISupplements, Supplements } from '../supplements.model';

import { SupplementsUpdateComponent } from './supplements-update.component';

describe('Supplements Management Update Component', () => {
  let comp: SupplementsUpdateComponent;
  let fixture: ComponentFixture<SupplementsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let supplementsService: SupplementsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SupplementsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SupplementsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SupplementsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    supplementsService = TestBed.inject(SupplementsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const supplements: ISupplements = { id: 'CBA' };

      activatedRoute.data = of({ supplements });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(supplements));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Supplements>>();
      const supplements = { id: 'ABC' };
      jest.spyOn(supplementsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplements });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: supplements }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(supplementsService.update).toHaveBeenCalledWith(supplements);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Supplements>>();
      const supplements = new Supplements();
      jest.spyOn(supplementsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplements });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: supplements }));
      saveSubject.complete();

      // THEN
      expect(supplementsService.create).toHaveBeenCalledWith(supplements);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Supplements>>();
      const supplements = { id: 'ABC' };
      jest.spyOn(supplementsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplements });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(supplementsService.update).toHaveBeenCalledWith(supplements);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
