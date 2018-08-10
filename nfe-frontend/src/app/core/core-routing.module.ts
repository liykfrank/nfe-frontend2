import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTES } from '../shared/constants/routes';
import { CoreComponent } from './core.component';

const routes: Routes = [
  {
    path: '', component: CoreComponent, children: [
      {
        path: '',
        loadChildren: '../menu/dashboard/dashboard.module#DashBoardModule',
        data: { tab: ROUTES.DASHBOARD }
      },
      {
        path: 'issue',
        loadChildren: '../menu/adm-acm/adm-acm.module#AdmAcmModule',
      },
      {
        path: ROUTES.REFUNDS.PATH,
        loadChildren: '../menu/refund/refund.module#RefundModule',
        data: { tab: ROUTES.REFUNDS }
      },
      {
        path: 'files',
        loadChildren: '../menu/files/files.module#FilesModule',
      },
      {
        path: ROUTES.CREATE_ACCOUNT.PATH,
        loadChildren: '../menu/sftp-accounts/sftp-accounts.module#SftpAccountsModule',
        data: { tab: ROUTES.CREATE_ACCOUNT }
      },
      {
        path: ROUTES.TICKETING.PATH,
        loadChildren: '../menu/empty/empty.module#EmptyModule',
        data: { tab: ROUTES.TICKETING }
      },
      {
        path: ROUTES.GENERAL_INFO.PATH,
        loadChildren: '../menu/empty/empty.module#EmptyModule',
        data: { tab: ROUTES.GENERAL_INFO }
      },
      {
        path: ROUTES.SELF_SERVICE.PATH,
        loadChildren: '../menu/empty/empty.module#EmptyModule',
        data: { tab: ROUTES.SELF_SERVICE }
      },
      {
        path: ROUTES.MONITOR.PATH,
        loadChildren: '../menu/monitor/monitor.module#MonitorModule',
        data: { tab: ROUTES.MONITOR }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule { }
