import { NgModule } from "@angular/core";
import { Routes, RouterModule } from '@angular/router';
import { WebComponent } from "./components/web/web.component";
import { TabParentComponent } from "./components/tab-parent/tab-parent.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: TabParentComponent
  },
  {
    path: 'web',
    component: WebComponent
  }
 /*  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'admin',
    canActivate: [AuthGuardService],
    loadChildren: '../admin/admin.module#AdminModule'
  },
  {
    path: 'form',
    loadChildren: '../form/form.module#FormModule'
  },
  {
    path: '**',
    component: NotFoundComponent
  } */
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule { }
