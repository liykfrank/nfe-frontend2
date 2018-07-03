

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SftpAccountsModule } from './sftp-accounts/sftp-accounts.module';
import { MonitorModule } from './monitor/monitor.module';
import { FilesModule } from './files/files.module';
import { AdmAcmModule } from './adm_acm/adm_acm.module';

@NgModule({
  imports: [
    CommonModule,
    FilesModule,
    MonitorModule,
    SftpAccountsModule,
    AdmAcmModule
  ],
  declarations: []
})

export class MenuModule { }
