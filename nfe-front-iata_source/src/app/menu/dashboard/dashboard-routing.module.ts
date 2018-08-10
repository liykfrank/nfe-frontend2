import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTES } from '../../shared/constants/routes';
import { DashboardComponent } from './dashboard.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: ROUTES.DASHBOARD.URL,
    pathMatch: 'full',
  },
  {
    path: ROUTES.DASHBOARD.PATH,
    component: DashboardComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashBoardRoutingModule { }
