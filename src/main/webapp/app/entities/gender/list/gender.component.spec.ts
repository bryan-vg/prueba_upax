import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GenderService } from '../service/gender.service';

import { GenderComponent } from './gender.component';

describe('Gender Management Component', () => {
  let comp: GenderComponent;
  let fixture: ComponentFixture<GenderComponent>;
  let service: GenderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GenderComponent],
    })
      .overrideTemplate(GenderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GenderService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.genders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
