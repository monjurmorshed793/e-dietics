import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComponentNavigationDetailComponent } from './component-navigation-detail.component';

describe('ComponentNavigation Management Detail Component', () => {
  let comp: ComponentNavigationDetailComponent;
  let fixture: ComponentFixture<ComponentNavigationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ComponentNavigationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ componentNavigation: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ComponentNavigationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ComponentNavigationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load componentNavigation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.componentNavigation).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
