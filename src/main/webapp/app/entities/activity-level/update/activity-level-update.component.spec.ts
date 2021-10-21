jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ActivityLevelService } from '../service/activity-level.service';
import { IActivityLevel, ActivityLevel } from '../activity-level.model';

import { ActivityLevelUpdateComponent } from './activity-level-update.component';

describe('ActivityLevel Management Update Component', () => {
  let comp: ActivityLevelUpdateComponent;
  let fixture: ComponentFixture<ActivityLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activityLevelService: ActivityLevelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ActivityLevelUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ActivityLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activityLevelService = TestBed.inject(ActivityLevelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const activityLevel: IActivityLevel = { id: 'CBA' };

      activatedRoute.data = of({ activityLevel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(activityLevel));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivityLevel>>();
      const activityLevel = { id: 'ABC' };
      jest.spyOn(activityLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityLevel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(activityLevelService.update).toHaveBeenCalledWith(activityLevel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivityLevel>>();
      const activityLevel = new ActivityLevel();
      jest.spyOn(activityLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityLevel }));
      saveSubject.complete();

      // THEN
      expect(activityLevelService.create).toHaveBeenCalledWith(activityLevel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActivityLevel>>();
      const activityLevel = { id: 'ABC' };
      jest.spyOn(activityLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activityLevelService.update).toHaveBeenCalledWith(activityLevel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
