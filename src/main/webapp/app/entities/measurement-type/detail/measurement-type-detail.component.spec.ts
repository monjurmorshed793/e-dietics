import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MeasurementTypeDetailComponent } from './measurement-type-detail.component';

describe('MeasurementType Management Detail Component', () => {
  let comp: MeasurementTypeDetailComponent;
  let fixture: ComponentFixture<MeasurementTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeasurementTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ measurementType: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(MeasurementTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MeasurementTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load measurementType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.measurementType).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
