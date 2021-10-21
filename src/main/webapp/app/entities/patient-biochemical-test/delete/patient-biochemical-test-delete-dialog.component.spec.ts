jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PatientBiochemicalTestService } from '../service/patient-biochemical-test.service';

import { PatientBiochemicalTestDeleteDialogComponent } from './patient-biochemical-test-delete-dialog.component';

describe('PatientBiochemicalTest Management Delete Component', () => {
  let comp: PatientBiochemicalTestDeleteDialogComponent;
  let fixture: ComponentFixture<PatientBiochemicalTestDeleteDialogComponent>;
  let service: PatientBiochemicalTestService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientBiochemicalTestDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PatientBiochemicalTestDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientBiochemicalTestDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PatientBiochemicalTestService);
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
