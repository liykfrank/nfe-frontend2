import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SftpAccountComponent } from './sftp-account.component';


const routes: Routes = [
      {
        path: '',
        component: SftpAccountComponent
      }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SftpAccountsRoutingModule { }
