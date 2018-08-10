import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: './core/core.module#CoreModule'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash : true, enableTracing: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
