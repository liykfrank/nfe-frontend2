import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTES } from '../../shared/constants/routes';
import { AdmAcmComponent } from './adm-acm.component';

const routes: Routes = [
  {
    path: ROUTES.ADM_ISSUE.PATH,
    component: AdmAcmComponent,
    data: {
      tab: ROUTES.ADM_ISSUE,
    }
  },
  {
    path: ROUTES.ACM_ISSUE.PATH,
    component: AdmAcmComponent,
    data: {
      tab: ROUTES.ACM_ISSUE,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdmAcmRoutingModule { }
