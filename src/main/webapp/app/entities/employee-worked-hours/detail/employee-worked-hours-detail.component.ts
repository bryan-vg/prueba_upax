import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeWorkedHours } from '../employee-worked-hours.model';

@Component({
  selector: 'jhi-employee-worked-hours-detail',
  templateUrl: './employee-worked-hours-detail.component.html',
})
export class EmployeeWorkedHoursDetailComponent implements OnInit {
  employeeWorkedHours: IEmployeeWorkedHours | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeWorkedHours }) => {
      this.employeeWorkedHours = employeeWorkedHours;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
