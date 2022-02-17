import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee-worked-hours',
        data: { pageTitle: 'pruebaupaxApp.employeeWorkedHours.home.title' },
        loadChildren: () => import('./employee-worked-hours/employee-worked-hours.module').then(m => m.EmployeeWorkedHoursModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'pruebaupaxApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'pruebaupaxApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'gender',
        data: { pageTitle: 'pruebaupaxApp.gender.home.title' },
        loadChildren: () => import('./gender/gender.module').then(m => m.GenderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
