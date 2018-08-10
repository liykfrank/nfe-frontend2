import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SftpAccountComponent } from './sftp-account.component';
import { SftpAccountsRoutingModule } from './sftp-accounts-routing.module';
import { SharedModule } from '../../shared/shared.module';


@NgModule({
  imports: [SftpAccountsRoutingModule, SharedModule],
  declarations: [ SftpAccountComponent ],
  entryComponents: [ SftpAccountComponent ]
})
export class SftpAccountsModule {}
