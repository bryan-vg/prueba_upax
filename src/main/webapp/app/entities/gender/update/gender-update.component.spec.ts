import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GenderService } from '../service/gender.service';
import { IGender, Gender } from '../gender.model';

import { GenderUpdateComponent } from './gender-update.component';

describe('Gender Management Update Component', () => {
  let comp: GenderUpdateComponent;
  let fixture: ComponentFixture<GenderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let genderService: GenderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GenderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GenderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    genderService = TestBed.inject(GenderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gender: IGender = { id: 456 };

      activatedRoute.data = of({ gender });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gender));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gender>>();
      const gender = { id: 123 };
      jest.spyOn(genderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gender });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gender }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(genderService.update).toHaveBeenCalledWith(gender);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gender>>();
      const gender = new Gender();
      jest.spyOn(genderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gender });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gender }));
      saveSubject.complete();

      // THEN
      expect(genderService.create).toHaveBeenCalledWith(gender);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gender>>();
      const gender = { id: 123 };
      jest.spyOn(genderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gender });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(genderService.update).toHaveBeenCalledWith(gender);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
