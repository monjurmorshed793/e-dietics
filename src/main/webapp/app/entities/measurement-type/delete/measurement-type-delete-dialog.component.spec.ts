jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MeasurementTypeService } from '../service/measurement-type.service';

import { MeasurementTypeDeleteDialogComponent } from './measurement-type-delete-dialog.component';

describe('MeasurementType Management Delete Component', () => {
  let comp: MeasurementTypeDeleteDialogComponent;
  let fixture: ComponentFixture<MeasurementTypeDeleteDialogComponent>;
  let service: MeasurementTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeasurementTypeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MeasurementTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MeasurementTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MeasurementTypeService);
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
