import { Routes, RouterModule } from '@angular/router';
import { ROUTES } from '../../../../shared/constants/routes';
import { UserComponent } from './user.component';
import { NgModule } from '@angular/core';


const routes: Routes = [
    {
      path: ROUTES.NEW_USER.PATH,
      component: UserComponent,
      data: {
        tab: ROUTES.NEW_USER,
      }
    },
    {
      path: ROUTES.NEW_SUB_USER.PATH,
      component: UserComponent,
      data: {
        tab: ROUTES.NEW_SUB_USER,
      }
    },
    {
      path: ROUTES.MOD_USER.PATH,
      component: UserComponent,
      data: {
        tab: ROUTES.MOD_USER,
      }
    },
    {
      path: ROUTES.MOD_SUB_USER.PATH,
      component: UserComponent,
      data: {
        tab: ROUTES.MOD_SUB_USER,
      }
    },
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
