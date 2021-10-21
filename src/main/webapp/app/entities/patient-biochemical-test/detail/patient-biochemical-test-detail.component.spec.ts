import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PatientBiochemicalTestDetailComponent } from './patient-biochemical-test-detail.component';

describe('PatientBiochemicalTest Management Detail Component', () => {
  let comp: PatientBiochemicalTestDetailComponent;
  let fixture: ComponentFixture<PatientBiochemicalTestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientBiochemicalTestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ patientBiochemicalTest: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(PatientBiochemicalTestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientBiochemicalTestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load patientBiochemicalTest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.patientBiochemicalTest).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
