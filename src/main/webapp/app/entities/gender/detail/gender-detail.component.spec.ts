import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GenderDetailComponent } from './gender-detail.component';

describe('Gender Management Detail Component', () => {
  let comp: GenderDetailComponent;
  let fixture: ComponentFixture<GenderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GenderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gender: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GenderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GenderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gender on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gender).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
