import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MonitorComponent } from './monitor.component';
import { ROUTES } from '../../shared/constants/routes';

const routes: Routes = [
      {
        path: '',
        component: MonitorComponent,
        data: { tab: ROUTES.MONITOR }
      }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MonitorRoutingModule { }
