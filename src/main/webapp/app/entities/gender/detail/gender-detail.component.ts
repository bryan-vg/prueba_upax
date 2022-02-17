import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGender } from '../gender.model';

@Component({
  selector: 'jhi-gender-detail',
  templateUrl: './gender-detail.component.html',
})
export class GenderDetailComponent implements OnInit {
  gender: IGender | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gender }) => {
      this.gender = gender;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
