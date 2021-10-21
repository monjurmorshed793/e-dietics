jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { NutritionStateService } from '../service/nutrition-state.service';

import { NutritionStateDeleteDialogComponent } from './nutrition-state-delete-dialog.component';

describe('NutritionState Management Delete Component', () => {
  let comp: NutritionStateDeleteDialogComponent;
  let fixture: ComponentFixture<NutritionStateDeleteDialogComponent>;
  let service: NutritionStateService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NutritionStateDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(NutritionStateDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NutritionStateDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NutritionStateService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
