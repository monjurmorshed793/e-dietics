jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BiochemicalTestService } from '../service/biochemical-test.service';

import { BiochemicalTestDeleteDialogComponent } from './biochemical-test-delete-dialog.component';

describe('BiochemicalTest Management Delete Component', () => {
  let comp: BiochemicalTestDeleteDialogComponent;
  let fixture: ComponentFixture<BiochemicalTestDeleteDialogComponent>;
  let service: BiochemicalTestService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BiochemicalTestDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(BiochemicalTestDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BiochemicalTestDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BiochemicalTestService);
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
